package com.murilofb.instaclone.helper

import android.content.Intent
import android.Manifest
import android.net.Uri
import android.provider.MediaStore

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.murilofb.instaclone.BuildConfig
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.BottomDialogPhotoOptionsBinding
import com.murilofb.instaclone.databinding.HeaderProfileBinding
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File


class MyLifeCycleObserver(private val activity: AppCompatActivity) : DefaultLifecycleObserver {
    /*
    OBSERVER PARA PODER SETTAR O ACTIVITY RESULT LAUNCHER NO ON CREATE DA ACTIVITY

    TENTAR SETTAR PARA O CORP ACTIVITY TAMBÉM

     */
    companion object {
        var bottomSheetDialog: BottomSheetDialog? = null
    }

    private lateinit var getGalleryContent: ActivityResultLauncher<Intent>
    private lateinit var getCameraContent: ActivityResultLauncher<Intent>


    override fun onCreate(owner: LifecycleOwner) {
        getGalleryContent = activity.activityResultRegistry.register(
            "key1",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val newPhotoUri = it.data!!.data!!
                CropImage.activity(newPhotoUri)
                    .setAspectRatio(1, 1)
                    .start(activity)
            }
        }

        getCameraContent = activity.activityResultRegistry.register(
            "key2",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                //val imageBitmap = it.data!!.extras!!.get("data")
                val imageUri = Uri.fromFile(File(CameraHelper.currentPhotoPath))

                CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(activity)
            }
        }
        super.onCreate(owner)
    }

    fun showPhotoOptionsWithoutRemove() {
        bottomSheetDialog =
            BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)

        val bottomDialogBinding = BottomDialogPhotoOptionsBinding.bind(
            activity.layoutInflater.inflate(
                R.layout.bottom_dialog_photo_options,
                activity.findViewById(R.id.photoOptionsContainer)
            )
        )
        bottomDialogBinding.optionRemove.isVisible = false
        bottomDialogBinding.optionGallery.setOnClickListener { pickImageFromGallery() }
        bottomDialogBinding.optionCamera.setOnClickListener { pickImageFromCamera() }
        bottomSheetDialog!!.setContentView(bottomDialogBinding.root)
        bottomSheetDialog!!.show()
    }

    //TAVEZ FAZER ALGO ASSIM
    fun pickImageFromGallery() {
        if (Permissions.validatePermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val galleryPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryPhotoIntent.type = "image/*"
            getGalleryContent.launch(galleryPhotoIntent)
            bottomSheetDialog?.dismiss()
        }
    }

    fun pickImageFromCamera() {
        if (Permissions.validatePermission(activity, Manifest.permission.CAMERA)) {
            val galleryPhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imageFile = CameraHelper(activity).createFile()
            val imageUri =
                FileProvider.getUriForFile(
                    activity,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    imageFile
                )
            galleryPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            getCameraContent.launch(galleryPhotoIntent)
            bottomSheetDialog?.dismiss()
        }
    }
}
package com.murilofb.instaclone.helper

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.murilofb.instaclone.BuildConfig
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.BottomDialogPhotoOptionsBinding
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class ImageChooser(private var activity: AppCompatActivity) {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var imageView: ShapeableImageView
    private lateinit var previousPhotoUri: Uri


    private val getGalleryContent =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val newPhotoUri = it.data!!.data!!
                CropImage.activity(newPhotoUri)
                    .setAspectRatio(1, 1)
                    .start(activity)
            }
        }
    private val getCameraContent =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                //val imageBitmap = it.data!!.extras!!.get("data")
                val imageUri = Uri.fromFile(File(CameraHelper.currentPhotoPath))

                CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(activity)
            }
        }

    fun takePictureClickListener() {

    }

    //Set the ImageView where the image will be replaced
    fun setImageView(imageView: ShapeableImageView): ImageChooser {
        this.imageView = imageView
        return this
    }

    // Set the previous image uri to set back in case you want to
    fun setPreviousPhotoUri(previousPhotoUri: Uri): ImageChooser {
        this.previousPhotoUri = previousPhotoUri
        return this
    }

    fun showPhotoOptions() {
        bottomSheetDialog =
            BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        val bottomDialogBinding = BottomDialogPhotoOptionsBinding.bind(
            activity.layoutInflater.inflate(
                R.layout.bottom_dialog_photo_options,
                activity.findViewById(R.id.photoOptionsContainer)
            )
        )
        bottomDialogBinding.optionRemove.setOnClickListener {
            if (previousPhotoUri != null) removeImageWithCancelOption()
            else removeImageFrom()
        }
        bottomDialogBinding.optionGallery.setOnClickListener {
            if (Permissions.validatePermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                pickImageFromGallery()
        }
        bottomDialogBinding.optionCamera.setOnClickListener {
            if (Permissions.validatePermission(activity, Manifest.permission.CAMERA))
                pickImageFromCamera()
        }
        bottomSheetDialog.setContentView(bottomDialogBinding.root)
        bottomSheetDialog.show()
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
        bottomDialogBinding.optionGallery.setOnClickListener { getGalleryContent?.let { pickImageFromGallery() } }
        bottomDialogBinding.optionCamera.setOnClickListener { getCameraContent?.let { pickImageFromCamera() } }
        bottomSheetDialog.setContentView(bottomDialogBinding.root)
        bottomSheetDialog.show()
    }


    private fun removeImageWithCancelOption() {
        Glide.with(activity)
            .load(R.drawable.default_profile_image)
            .into(imageView)
        showConfirmRemoveSnackBar()
        bottomSheetDialog.dismiss()
    }

    private fun removeImageFrom() {
        Glide.with(activity)
            .load(R.drawable.default_profile_image)
            .into(imageView)
        RealtimeDatabaseHelper.removeUserPhoto()
        bottomSheetDialog.dismiss()
    }

    private fun pickImageFromGallery() {
        val galleryPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryPhotoIntent.type = "image/*"
        getGalleryContent.launch(galleryPhotoIntent)
        bottomSheetDialog.dismiss()
    }

    private fun pickImageFromCamera() {
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
        bottomSheetDialog.dismiss()
    }

    private fun showConfirmRemoveSnackBar() {
        val snackConfirmRemove = Snackbar.make(
            activity.findViewById(android.R.id.content),
            activity.getString(R.string.remove_photo_confirmation),
            Snackbar.LENGTH_SHORT
        )

        snackConfirmRemove.setAction(activity.getString(R.string.undo)) { }
        /*
        Adcionando callback para caso tenha sido clicado o undo não remover de fato a imagem
        e voltar a exibir ela no Image View
         */
        snackConfirmRemove.addCallback(EditProfileImageCallBack(activity, previousPhotoUri))
        snackConfirmRemove.show()
    }
}



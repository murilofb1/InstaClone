package com.murilofb.instaclone.ui.home.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.ActivityProfileConfigurationBinding
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.firebase.StorageHelper
import com.murilofb.instaclone.helper.ImageChooser
import com.murilofb.instaclone.helper.ToastHelper
import com.murilofb.instaclone.model.UserModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE

class EditProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileConfigurationBinding
    lateinit var currentUserModel: UserModel
    private lateinit var toast: ToastHelper
    private lateinit var bottomSheetDialog: BottomSheetDialog

    companion object {
        private lateinit var newPhotoUri: Uri
    }

    private lateinit var imageChooser: ImageChooser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toast = ToastHelper(this)
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        imageChooser = ImageChooser(this)
        setCustomToolbar()
        initComponents()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    //Activity Result to get the result from ImageCropper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            Glide.with(applicationContext)
                .load(result.uri)
                .into(binding.imgProfileEdit)
            StorageHelper.uploadProfilePhoto(result.uri)
            newPhotoUri = result.uri
            bottomSheetDialog.dismiss()
        }
    }

    private fun setCustomToolbar() {
        val toolbar = binding.toolbarProfile.customToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponents() {
        insertComponentsData()
        setClickListeners()
    }

    private fun insertComponentsData() {
        RealtimeDatabaseHelper.getCurrentUser().observe(this, {
            if (it != null) {
                currentUserModel = it
                binding.edtUserNameEdit.setText(it.username)
                binding.edtEmailEdit.setText(it.email)
                //Alterar para a foto passada ao inves de carregar do firebase caso tenha sido trocada
                if (StorageHelper.isUploadingPhoto) {
                    Glide.with(this)
                        .load(newPhotoUri)
                        .placeholder(R.drawable.default_profile_image)
                        .into(binding.imgProfileEdit)
                } else {
                    Glide.with(this)
                        .load(Uri.parse(it.photoLink))
                        .placeholder(R.drawable.default_profile_image)
                        .into(binding.imgProfileEdit)
                }
            }
        })
    }

    //Variavel para a nova foto selecionada
    private fun setClickListeners() {
        //Botão de confirmar
        binding.btnConfirmChanges.setOnClickListener {
            val email = binding.edtEmailEdit.text.toString()
            val username = binding.edtUserNameEdit.text.toString()
            if (email.isNotEmpty() && username.isNotEmpty()) {
                currentUserModel.email = email
                currentUserModel.username = username.lowercase()
                RealtimeDatabaseHelper.updateUserInfo(currentUserModel)
                finish()
            } else { toast.showToast(getString(R.string.fill_all)) }
        }
        //Botão de alterar a foto
        binding.txtChangePhoto.setOnClickListener {
            imageChooser
                .setImageView(binding.imgProfileEdit)
                .setPreviousPhotoUri(Uri.parse(currentUserModel.photoLink))
                .showPhotoOptions()
        }
    }
}
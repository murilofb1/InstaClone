package com.murilofb.instaclone.helper

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.murilofb.instaclone.R
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.ui.home.profile.EditProfileActivity

/*
CallBack customizado para cuidar de quando o usuário quiser desfazer a remoção da foto de perfil
 */
class EditProfileImageCallBack(
    private var activity: AppCompatActivity,
    private var previousImage: Uri
) :
    Snackbar.Callback() {

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        if (event == DISMISS_EVENT_ACTION) {
            val profileActivity = activity as EditProfileActivity
            Glide.with(activity.applicationContext)
                .load(previousImage)
                .placeholder(R.drawable.default_profile_image)
                .into(profileActivity.binding.imgProfileEdit)
        } else {
            RealtimeDatabaseHelper.removeUserPhoto()
        }
    }

}
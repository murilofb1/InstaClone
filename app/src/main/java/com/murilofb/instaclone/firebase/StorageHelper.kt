package com.murilofb.instaclone.firebase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.murilofb.instaclone.constants.StorageKeys
import com.murilofb.instaclone.helper.ImageConverter
import com.murilofb.instaclone.model.Post

class StorageHelper {

    companion object {
        var isUploadingPhoto = false

        private val rootReference = FirebaseStorage.getInstance().reference


        fun uploadProfilePhoto(uri: Uri) {
            isUploadingPhoto = true
            val currentUserId = RealtimeDatabaseHelper.getCurrentUser().value!!.id
            val profilePicturePath = "${StorageKeys.PROFILE_IMAGES_KEY}/${currentUserId}.png"
            rootReference.child(profilePicturePath)
                .putFile(uri)
                .addOnSuccessListener {
                    isUploadingPhoto = false
                    rootReference.child(profilePicturePath).downloadUrl.addOnSuccessListener {
                        RealtimeDatabaseHelper.changeProfilePicture(
                            profilePicturePath,
                            it.toString()
                        )
                    }
                }
        }

        fun getDownloadUrlInThePath(imagePath: String): Task<Uri> {
            val storageReference = FirebaseStorage.getInstance().reference.child(imagePath)
            return storageReference.downloadUrl
        }


        fun uploadPostPhoto(post: Post): UploadTask {
            val currentUserId = FirebaseAuthH.getCurrentUser()!!.uid
            val byteArr = ImageConverter.bitmapToByteArray(post.imageBitmap!!)
            val postPath = "${StorageKeys.POSTS_IMAGES_KEY}/${currentUserId}/${post.photoId}.png"
            post.postPath = postPath
            return rootReference.child(postPath).putBytes(byteArr)
        }
    }
}
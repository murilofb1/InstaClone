package com.murilofb.instaclone.ui.home.newpost

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.murilofb.instaclone.R
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.firebase.StorageHelper
import com.murilofb.instaclone.helper.ToastHelper
import com.murilofb.instaclone.model.Post

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    private val tryingToPost = MutableLiveData<Boolean>()
    fun isTryingToPost(): LiveData<Boolean> = tryingToPost
    private val postSuccessful = MutableLiveData<Boolean>()
    fun isPostSuccessful(): LiveData<Boolean> = postSuccessful
    private val toastMessage = MutableLiveData<String>()
    fun getToastMessage(): LiveData<String> = toastMessage

    fun postPhoto(post: Post) {
        tryingToPost.value = true

        StorageHelper.uploadPostPhoto(post).addOnCompleteListener { storageUpload ->
            if (storageUpload.isSuccessful) {
                PostsDB.addPostToRealtimeDB(post).addOnCompleteListener { databaseUpload ->
                    if (databaseUpload.isSuccessful) {
                        postSuccessful.value = true
                        toastMessage.value =
                            getApplication<Application>().getString(R.string.success_post)
                    } else {
                        toastMessage.value =
                            getApplication<Application>().getString(R.string.failure_post)
                    }
                    tryingToPost.value = false
                }
            } else {
                tryingToPost.value = false
                toastMessage.value =
                    getApplication<Application>().getString(R.string.failure_post)
            }
        }
    }

}
package com.murilofb.instaclone.helper

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ToastHelper(var activity: AppCompatActivity) {
    private var toast: Toast

    init {
        toast = Toast(activity.applicationContext)
    }

    fun showToast(message: String) {
        if (message.isNotEmpty()) {
            toast.cancel()
            toast = Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

}
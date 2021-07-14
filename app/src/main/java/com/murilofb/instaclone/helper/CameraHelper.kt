package com.murilofb.instaclone.helper

import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private var activity: AppCompatActivity) {
    companion object {
        lateinit var currentPhotoPath: String
    }

    @Throws(IOException::class)
    fun createFile(): File {
        //Create an image file name
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmsss").format(Date())
        val storageDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDirectory
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


}
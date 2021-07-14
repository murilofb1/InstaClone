package com.murilofb.instaclone.helper

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class ImageConverter {
    companion object {
        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos)
            return baos.toByteArray()
        }
    }
}
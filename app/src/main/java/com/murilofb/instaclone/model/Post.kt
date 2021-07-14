package com.murilofb.instaclone.model

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import java.io.Serializable
import java.util.*

data class Post(
    var description: String? = null,
    var photoLink: String? = null,
    var likeCount: Int = 0,
    var photoId: String? = null,
    var user: UserModel? = null,
    var postTimestamp: Long = 0L,
    @get:Exclude var imageBitmap: Bitmap? = null,
    @get:Exclude var postPath: String? = null,
) : Serializable {

    init {
        if (photoId == null) photoId = PostsDB.getCurrentUserPostsReference().push().key!!
        if (user == null) user = RealtimeDatabaseHelper.getCurrentUser().value!!
        if (postTimestamp == 0L) postTimestamp = Date().time
    }
}

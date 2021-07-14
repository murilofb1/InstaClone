package com.murilofb.instaclone.model

import com.google.firebase.database.Exclude
import com.murilofb.instaclone.constants.RealtimeConstants
import java.io.Serializable

class Comment(@get:Exclude val post: Post?, var user: UserModel?, var comment: String = "") :
    Serializable {
    var id: String = ""

    constructor() : this(null, null, "")

    init {
        if (id.isEmpty() && post != null) {
            id = RealtimeConstants.commentsReference.child(post!!.photoId!!).push().key!!
        }
    }

}
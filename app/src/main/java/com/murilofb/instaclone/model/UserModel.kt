package com.murilofb.instaclone.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class UserModel(
    var email: String,
    @get:Exclude var password: String,
    var username: String,
    var postsCount: Int = 0,
    var followersCount: Int = 0,
    var followingCount: Int = 0
) : Serializable {
    lateinit var id: String
    var photoLink: String = ""

    constructor() : this("", "", "")

    // fun getPhotoPath(): String = "${StorageKeys.PROFILE_IMAGES_KEY}/${id}.png"

}

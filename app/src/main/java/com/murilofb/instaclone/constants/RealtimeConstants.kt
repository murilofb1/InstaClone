package com.murilofb.instaclone.constants

import com.google.firebase.database.FirebaseDatabase

object RealtimeConstants {
    //User
    private const val USERS_KEY = "users"
    const val USERNAME_KEY = "username"
    const val PROFILE_PICTURE_LINK_KEY = "photoLink"
    const val USER_FOLLOWERS_COUNT_KEY = "followersCount"
    const val USER_FOLLOWING_COUNT_KEY = "followingCount"
    const val USER_POSTS_COUNT_KEY = "postsCount"

    //Followers
    private const val FOLLOWERS_KEY = "followers"
    private const val FOLLOWING_KEY = "following"

    //Comments
    private const val COMMENTS_KEY = "comments"

    //Likes
    private const val LIKES_KEY = "likes"
     const val LIKE_COUNT_KEY = "likeCount"


    //Posts
    private const val POSTS_KEY = "posts"

    //Images
    private const val IMAGES_KEY = "images"
    private const val IMAGES_PATHS_KEY = "paths"
    private const val IMAGES_LINKS_KEY = "downloadLinks"
    const val PROFILE_PICTURE_PATH_KEY = "profile"

    //References
    private val rootReference = FirebaseDatabase.getInstance().reference
    val followersRootReference = rootReference.child(FOLLOWERS_KEY)
    val likesRootReference = rootReference.child(LIKES_KEY)
    val commentsReference = rootReference.child(COMMENTS_KEY)
    val usersRootReference = rootReference.child(USERS_KEY)
    val imagesPathReference = rootReference.child(IMAGES_KEY)
        .child(IMAGES_PATHS_KEY)
    val imagesLinkReference = rootReference.child(IMAGES_KEY)
        .child(IMAGES_LINKS_KEY)

    val postsRootReference = rootReference.child(POSTS_KEY)

}
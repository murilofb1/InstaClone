package com.murilofb.instaclone.firebase

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.constants.RealtimeConstants
import com.murilofb.instaclone.model.Post

class PostsDB : RealtimeDB() {
    companion object {
        fun getCurrentUserPostsReference(): DatabaseReference {
            val currentUser = FirebaseAuthH.getCurrentUser()
            return RealtimeConstants.postsRootReference.child(currentUser!!.uid)
        }

        private fun getCurrentUserLikesReference(): DatabaseReference {
            val currentUser = FirebaseAuthH.getCurrentUser()
            return RealtimeConstants.likesRootReference.child(currentUser!!.uid)
        }

        fun addPostToRealtimeDB(post: Post): Task<Uri> {
            val getUrlListener = OnCompleteListener<Uri> {
                post.photoLink = it.result.toString()
                getCurrentUserPostsReference().child(post.photoId!!)
                    .setValue(post)
                    .addOnCompleteListener { setValueTask ->
                        if (setValueTask.isSuccessful) {
                            RealtimeDatabaseHelper.getCurrentUserImagesPathReference()
                                .child(post.photoId!!)
                                .setValue(post.postPath)
                            RealtimeDatabaseHelper.incrementPostsCount()
                        }
                    }
            }

            return StorageHelper.getDownloadUrlInThePath(post.postPath!!)
                .addOnCompleteListener(getUrlListener)
        }
    }

    fun haveILikedThePost(postId: String, eventListener: ValueEventListener) {
        val reference = RealtimeConstants.likesRootReference
            .child(FirebaseAuthH.getCurrentUserID()!!)
            .child(postId)
        reference.addValueEventListener(eventListener)
        addConsult(reference, eventListener)
    }

    fun likePost(post: Post) {
        RealtimeConstants.likesRootReference
            .child(FirebaseAuthH.getCurrentUserID()!!)
            .child(post.photoId!!)
            .setValue(true)
        val newLikesCount = (post.likeCount) + 1
        RealtimeConstants.postsRootReference
            .child(post.user!!.id)
            .child(post.photoId!!)
            .child(RealtimeConstants.LIKE_COUNT_KEY)
            .setValue(newLikesCount)
    }

    fun unlikePost(post: Post) {
        RealtimeConstants.likesRootReference
            .child(FirebaseAuthH.getCurrentUserID()!!)
            .child(post.photoId!!)
            .removeValue()

        val newLikesCount = (post.likeCount) - 1
        RealtimeConstants.postsRootReference
            .child(post.user!!.id)
            .child(post.photoId!!)
            .child(RealtimeConstants.LIKE_COUNT_KEY)
            .setValue(newLikesCount)
    }

    fun loadPostsLiked(eventListener: ValueEventListener) {
        val reference = getCurrentUserLikesReference()
        reference.addValueEventListener(eventListener)
        addConsult(reference, eventListener)
    }

    fun loadPostById(userId: String, postId: String, eventListener: ValueEventListener) {
        val reference = RealtimeConstants.postsRootReference.child(userId).child(postId)
        reference.addValueEventListener(eventListener)
        addConsult(reference, eventListener)
    }

    fun loadPostsFromUser(userId: String, valueEventListener: ValueEventListener) {
        val reference = RealtimeConstants.postsRootReference.child(userId)
        reference.addValueEventListener(valueEventListener)
        addConsult(reference, valueEventListener)
    }


}

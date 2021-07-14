package com.murilofb.instaclone.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.constants.RealtimeConstants
import com.murilofb.instaclone.model.Comment
import com.murilofb.instaclone.model.UserModel

class CommentsDB : RealtimeDB() {

    fun pushComment(comment: Comment) {
        val postId = comment.post!!.photoId
        val currentUserId = FirebaseAuthH.getCurrentUser()!!.uid
        val userFb = UsersDB()
        userFb.getUserById(currentUserId, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comment.user = snapshot.getValue(UserModel::class.java)
                RealtimeConstants.commentsReference.child(postId!!).child(comment.id)
                    .setValue(comment)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    fun getCommentsForPost(postId: String, listener: ValueEventListener) {
        val reference = RealtimeConstants.commentsReference.child(postId!!)
        reference.addValueEventListener(listener)
        addConsult(reference, listener)
    }
}
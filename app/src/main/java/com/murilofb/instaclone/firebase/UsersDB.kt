package com.murilofb.instaclone.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.constants.RealtimeConstants
import com.murilofb.instaclone.model.UserModel

class UsersDB : RealtimeDB() {

    fun getUserById(userID: String, valueEventListener: ValueEventListener) {
        val reference = RealtimeConstants.usersRootReference.child(userID)
        reference.addValueEventListener(valueEventListener)
        addConsult(reference, valueEventListener)
    }

    fun searchUserByUserName(userName: String, eventListener: ValueEventListener) {
        var userName = userName.lowercase()
        Log.i("SearchFragment", "Seached by: $userName")
        val reference = RealtimeConstants.usersRootReference
            .orderByChild(RealtimeConstants.USERNAME_KEY)
            .startAt(userName)
            .endAt("$userName\uf8ff ")
        reference.addValueEventListener(eventListener)
        addConsult(reference, eventListener)
    }

    fun addFollowerToTheUser(userId: String) {
        RealtimeConstants.usersRootReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}
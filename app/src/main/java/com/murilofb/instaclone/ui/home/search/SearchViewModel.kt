package com.murilofb.instaclone.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.firebase.UsersDB
import com.murilofb.instaclone.model.UserModel

class SearchViewModel() : ViewModel() {
    private val usersList = MutableLiveData<MutableList<UserModel>>()
    private val usersDB = UsersDB()

    init {
        usersList.value = ArrayList()
    }

    fun getUsersList(): LiveData<MutableList<UserModel>> = usersList

    fun searchForUser(username: String?) {
        if (username!!.isEmpty()) {
            usersList.value = ArrayList()
        } else {
            usersDB.searchUserByUserName(username, object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<UserModel>()
                    for (item in snapshot.children) {
                        val user = item.getValue(UserModel::class.java)!!
                        if (user.id != FirebaseAuthH.getCurrentUserID()) list.add(user)
                    }
                    usersList.value = list
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}
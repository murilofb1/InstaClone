package com.murilofb.instaclone.ui.home.profile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.firebase.UsersDB
import com.murilofb.instaclone.model.Post
import com.murilofb.instaclone.model.UserModel


class ProfileViewModel : ViewModel() {

    private val postsDB = PostsDB()
    private val usersDB = UsersDB()

    private val myPosts = MutableLiveData<List<Post>>()
    fun getMyPosts(): LiveData<List<Post>> = myPosts
    private val currentUser = MutableLiveData<UserModel>()
    fun getCurrentUser(): LiveData<UserModel> = currentUser

    init {

    }

    private fun loadMyPosts() {
        val myId = FirebaseAuthH.getCurrentUser()!!.uid
        postsDB.loadPostsFromUser(myId, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Post> = ArrayList()
                for (item in snapshot.children) {
                    list.add(item.getValue(Post::class.java)!!)
                }
                myPosts.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadMyInfo() {
        val myId = FirebaseAuthH.getCurrentUser()!!.uid
        usersDB.getUserById(myId, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser.value = snapshot.getValue(UserModel::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    fun attachListeners(){
        loadMyInfo()
        loadMyPosts()
    }

    fun detachListeners() {
        postsDB.removeAllListeners()
        usersDB.removeAllListeners()
    }
}
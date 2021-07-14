package com.murilofb.instaclone.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.firebase.FollowersDB
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.firebase.UsersDB
import com.murilofb.instaclone.model.Post
import com.murilofb.instaclone.model.UserModel

class UserProfileViewModel : ViewModel() {
    val user = MutableLiveData<UserModel>()
    val userPostsList = MutableLiveData<List<Post>>()
    private val following = MutableLiveData<Boolean>()
    private val postsDB = PostsDB()
    private val usersDB = UsersDB()
    private val followersDB = FollowersDB()

    fun setUser(user: UserModel) {
        this.user.value = user
        findPostsByUserId(user.id)
        findUserById(user.id)
    }

    private fun findUserById(userId: String) {
        usersDB.getUserById(userId, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user.value = snapshot.getValue(UserModel::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun findPostsByUserId(userId: String) {
        postsDB.loadPostsFromUser(userId, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts: MutableList<Post> = ArrayList()
                for (item: DataSnapshot in snapshot.children) {
                    posts.add(item.getValue(Post::class.java)!!)
                }
                userPostsList.value = posts
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun detachListeners() {
        postsDB.removeAllListeners()
        usersDB.removeAllListeners()
        followersDB.removeAllListeners()
    }

    fun amIFollowingIt(): LiveData<Boolean> {
        followersDB.amIFollowing(user.value!!.id, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                following.value = snapshot.exists()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return following
    }

    fun followIt() = followersDB.followUser(user.value!!)

    fun unfollowIt() = followersDB.unfollowUser(user.value!!)
    fun attachListeners() {
        TODO("Not yet implemented")
    }
}
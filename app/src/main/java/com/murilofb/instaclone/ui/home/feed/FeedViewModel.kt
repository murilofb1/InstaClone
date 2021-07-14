package com.murilofb.instaclone.ui.home.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.firebase.FollowersDB
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.model.Post

class FeedViewModel : ViewModel() {
    private val postsList = MutableLiveData<List<Post>>()
    fun getPostsList(): LiveData<List<Post>> = postsList
    private val likesList = MutableLiveData<List<String>>()
    fun getLikesList(): LiveData<List<String>> = likesList
    private val postsDB = PostsDB()
    private val followersDB = FollowersDB()

    /*
        fun loadPosts() {
            postsDB.loadPostsFromUser(FirebaseAuthH.getCurrentUserID()!!,
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list: MutableList<Post> = ArrayList()
                        for (item in snapshot.children) {
                            list.add(item.getValue(Post::class.java)!!)
                        }
                        postsList.value = list
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

     */
    val tempList = ArrayList<Post>()
    fun loadPosts() {
        followersDB.loadFollowersList(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val followersCount = snapshot.childrenCount
                //If the user is  following nobody clear the list
                if (!snapshot.exists()) postsList.value = ArrayList()

                var i = 0L
                for (item in snapshot.children) {
                    postsDB.loadPostsFromUser(item.key!!, object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            i++
                            for (postItem in snapshot.children) {
                                val post = postItem.getValue(Post::class.java)!!
                                checkDuplicatedPostAndInsert(post)
                            }
                            if (i >= followersCount) {
                                updatePosts()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun checkDuplicatedPostAndInsert(post: Post) {
        if (tempList.size == 0) {
            tempList.add(post)
        } else {
            var swapped = false
            var i = 0
            while (i < tempList.size) {
                val item = tempList[i]
                if (item.photoId == post.photoId) {
                    tempList.removeAt(i)
                    tempList.add(i, post)
                    swapped = true
                    break
                }
                i++
            }
            if (!swapped) tempList.add(post)
        }
    }

    private fun updatePosts() {
        postsList.value = tempList
    }

    fun loadLikes() {
        postsDB.loadPostsLiked(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<String> = ArrayList()
                for (item in snapshot.children) {
                    list.add(item.key!!)
                }
                likesList.value = list

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
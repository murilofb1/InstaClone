package com.murilofb.instaclone.ui.home.post

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.murilofb.instaclone.R
import com.murilofb.instaclone.firebase.CommentsDB
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.model.Comment
import com.murilofb.instaclone.model.Post
import com.murilofb.instaclone.model.UserModel


class PostViewModel(application: Application) : AndroidViewModel(application) {
    var user: UserModel? = null
    var post: Post? = null

    private val commentDb = CommentsDB()
    private val postsDb = PostsDB()
    lateinit var currentUser: UserModel

    private val toastMessage = MutableLiveData<String>()
    fun getToastMessage(): LiveData<String> = toastMessage
    private val commentsList = MutableLiveData<List<Comment>>()
    fun getCommentsList(): LiveData<List<Comment>> = commentsList
    private val iLikeIt = MutableLiveData<Boolean>()
    fun haveILikedIt(): LiveData<Boolean> = iLikeIt
    private val likeCount = MutableLiveData<Int>()
    fun getLikeCount(): LiveData<Int> = likeCount

    fun setCurrentPost(post: Post) {
        this.post = post
        likeCount.value = post.likeCount
    }

    fun observeChangesInThePost() {
        postsDb.loadPostById(user!!.id, post!!.photoId!!, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val returnedPost = snapshot.getValue(Post::class.java)
                likeCount.value = returnedPost!!.likeCount
                post?.likeCount = likeCount.value!!
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun publishComment(comment: Comment) {
        if (comment.comment.isNotEmpty()) commentDb.pushComment(comment)
        else toastMessage.value =
            getApplication<Application>().applicationContext.getString(R.string.toast_empty_comment)
    }


    fun loadCommentsList() {
        commentDb.getCommentsForPost(post!!.photoId!!, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Comment> = ArrayList()
                for (item: DataSnapshot in snapshot.children) {
                    list.add(item.getValue(Comment::class.java)!!)
                }
                commentsList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun observeLikeStatus() {
        postsDb.haveILikedThePost(post!!.photoId!!, object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                iLikeIt.value = snapshot.exists()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun likeIt() {
        postsDb.likePost(post!!)
    }

    fun unlikeIt() {
        postsDb.unlikePost(post!!)
    }

    fun detachListeners() {
        commentDb.removeAllListeners()
    }
}

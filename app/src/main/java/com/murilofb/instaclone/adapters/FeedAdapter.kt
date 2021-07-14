package com.murilofb.instaclone.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.murilofb.instaclone.R
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.RecyclerFeedPostsBinding
import com.murilofb.instaclone.firebase.FirebaseAuthH
import com.murilofb.instaclone.firebase.PostsDB
import com.murilofb.instaclone.model.Post
import com.murilofb.instaclone.ui.home.search.UserProfileActivity

class FeedAdapter(private val context: Context, private var postList: List<Post> = ArrayList()) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    lateinit var binding: RecyclerFeedPostsBinding
    private var likesList: List<String> = ArrayList()
    fun updatePostsList(newList: List<Post>) {
        postList = newList
        Log.i("FeedFragment","Posts List Changed")
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position

    fun updateLikesList(newList: List<String>) {
        likesList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        binding =
            RecyclerFeedPostsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FeedViewHolder(binding.root)
    }

    private val postsDB = PostsDB()
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        if (postList.isNotEmpty()) {
            holder.setIsRecyclable(false)
            val post = postList[position]
            with(binding) {
                rvFeedPostDescription.text = post.description
                simpleGlide(rvFeedPostImage, post.photoLink!!)
                rvFeedUsername.text = post.user!!.username
                simpleGlide(rvFeedUserProfilePicture, post.user!!.photoLink)
                layoutUserInfo.setOnClickListener { openUserAtThePosition(position) }

                if (haveILikedThePost(post.photoId!!)) {
                    rvFeedBtnLike.setImageResource(R.drawable.ic_liked)
                    rvFeedBtnLike.setOnClickListener { postsDB.unlikePost(post)
                    }
                } else {
                    rvFeedBtnLike.setImageResource(R.drawable.ic_unliked

                    )
                    rvFeedBtnLike.setOnClickListener { postsDB.likePost(post)
                    }
                }


            }
        }
    }

    //not working very well ¯\_(ツ)_/¯
    private fun haveILikedThePost(postId: String): Boolean {
        for (item:String in likesList) {
            if (item == postId) return true
        }
        return false
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        postsDB.removeAllListeners()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun simpleGlide(imageView: ShapeableImageView, imageLink: String) {
        val view = imageView as View
        Glide.with(view.context)
            .load(Uri.parse(imageLink))
            .placeholder(R.drawable.default_post_image)
            .into(imageView)
    }

    private fun openUserAtThePosition(position: Int) {
        val user = postList[position].user
        if (user!!.id != FirebaseAuthH.getCurrentUserID()) {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(IntentTags.EXTRA_USER, user)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postList.size

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
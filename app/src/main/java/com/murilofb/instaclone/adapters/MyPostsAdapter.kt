package com.murilofb.instaclone.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.RecyclerMyPostsBinding
import com.murilofb.instaclone.model.Post

class MyPostsAdapter(private var postsList: List<Post> = ArrayList()) :
    RecyclerView.Adapter<MyPostsAdapter.MyViewHolder>() {
    private lateinit var binding: RecyclerMyPostsBinding
    private var clickListener: View.OnClickListener? = null

    fun updateList(newPostsList: List<Post>) {
        postsList = newPostsList
        notifyDataSetChanged()
    }

    fun setOnClickListener(clickListener: View.OnClickListener?) {
        this.clickListener = clickListener
    }

    fun getItemAtPosition(position: Int): Post {
        return postsList[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = RecyclerMyPostsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        if (postsList.isNotEmpty()) {
            val currentPost = postsList[position]
            Glide.with(holder.itemView)
                .load(Uri.parse(currentPost.photoLink))
                .placeholder(R.drawable.default_post_image)
                .into(binding.myPostImage)
            if (clickListener != null) {
                binding.layoutPost.setOnClickListener(clickListener)
            }

        }
    }

    override fun getItemCount(): Int = postsList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
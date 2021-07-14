package com.murilofb.instaclone.adapters

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.RecyclerCommentBinding
import com.murilofb.instaclone.model.Comment

class AdapterComments(private var commentsList: List<Comment> = ArrayList()) :
    RecyclerView.Adapter<AdapterComments.CommentsViewHolder>() {

    lateinit var binding: RecyclerCommentBinding
    fun updateList(list: List<Comment>) {
        this.commentsList = list
        notifyDataSetChanged()
    }

    fun getListSize() = commentsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        binding = RecyclerCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        if (commentsList.isNotEmpty()) {
            holder.setIsRecyclable(false)
            val comment = commentsList[position]
            with(binding) {
                txtComment.text = comment.comment
                txtComment.setOnClickListener { toggleCommentSize(holder) }
                txtCommenterUserName.text = "${comment.user!!.username}:"
                Glide.with(binding.root)
                    .load(Uri.parse(comment.user!!.photoLink))
                    .into(imgCommenterPhoto)
            }
        }
    }

    private fun toggleCommentSize(holder: CommentsViewHolder) {
        with(holder.txtComment) {
            maxLines = when (maxLines) {
                1 -> Int.MAX_VALUE
                else -> 1
            }
        }
    }

    override fun getItemCount(): Int = commentsList.size

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtComment: MaterialTextView = itemView.findViewById(R.id.txtComment)
    }
}






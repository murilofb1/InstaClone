package com.murilofb.instaclone.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murilofb.instaclone.R
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.RecyclerUserLayoutBinding

import com.murilofb.instaclone.model.UserModel
import com.murilofb.instaclone.ui.home.search.UserProfileActivity

class AdapterUsersList(
    var activity: Activity, private var userList: MutableList<UserModel> = ArrayList()
) :
    RecyclerView.Adapter<AdapterUsersList.UserListViewHolder>() {

    fun updateList(list: MutableList<UserModel>) {
        userList = list
        notifyDataSetChanged()
    }

    lateinit var binding: RecyclerUserLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        binding = RecyclerUserLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        if (userList.isNotEmpty()) {
            val currentUser = userList[position]
            holder.setIsRecyclable(false)
            with(binding) {
                rvUsersUsername.text = currentUser.username
                Glide.with(holder.itemView)
                    .load(Uri.parse(currentUser.photoLink))
                    .placeholder(R.drawable.default_profile_image)
                    .into(rvUsersProfilePicture)
                root.setOnClickListener {
                    val intent =
                        Intent(activity.applicationContext, UserProfileActivity::class.java)
                    intent.putExtra(IntentTags.EXTRA_USER, currentUser)
                    activity.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
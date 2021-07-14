package com.murilofb.instaclone.ui.home.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murilofb.instaclone.R
import com.murilofb.instaclone.adapters.MyPostsAdapter
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.FragmentProfileBinding
import com.murilofb.instaclone.databinding.HeaderProfileBinding
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.ui.home.post.PostActivity

class ProfileFragment : Fragment() {

    companion object {
        val getInstance = ProfileFragment()
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var headerView: HeaderProfileBinding
    private val model: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initLayout()
        return binding.root
    }

    override fun onStart() {
        model.attachListeners()
        super.onStart()
    }

    override fun onDestroy() {
        model.detachListeners()
        super.onDestroy()
    }

    private fun initLayout() {
        headerView = binding.headerLayout
        headerView.btnActionProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }
        initUserData()
        initRecycler()
    }

    private fun initUserData() {
        //Getting the UserInfo
        model.getCurrentUser().observe(viewLifecycleOwner, {
            if (it != null) {
                //ProfilePic
                Glide.with(this)
                    .load(Uri.parse(it.photoLink))
                    .placeholder(R.drawable.default_profile_image)
                    .into(headerView.imgProfileImage)
                //Posts, Followers, Following
                with(headerView) {
                    txtPostsCount.text = it.postsCount.toString()
                    txtFollowersCount.text = it.followersCount.toString()
                    txtFollowingCount.text = it.followingCount.toString()
                }
            }
        })
        //Getting the User Posts
        model.getMyPosts().observe(viewLifecycleOwner, { adapter.updateList(it) })
    }

    private val adapter = MyPostsAdapter()
    private fun initRecycler() {
        with(binding.recyclerMyPosts) {
            adapter = this@ProfileFragment.adapter
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        }
        setCustomRecyclerClickListener()
    }

    private fun setCustomRecyclerClickListener() {
        adapter.setOnClickListener {
            val position = binding.recyclerMyPosts.getChildLayoutPosition(it)
            val post = adapter.getItemAtPosition(position)
            val currentUser = RealtimeDatabaseHelper.getCurrentUser().value
            val intent = Intent(activity?.baseContext, PostActivity::class.java)
            intent.putExtra(IntentTags.EXTRA_USER, currentUser)
            intent.putExtra(IntentTags.EXTRA_POST, post)
            activity?.startActivity(intent)
        }
    }


}
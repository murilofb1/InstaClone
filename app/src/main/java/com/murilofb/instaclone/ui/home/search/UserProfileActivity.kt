package com.murilofb.instaclone.ui.home.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murilofb.instaclone.R
import com.murilofb.instaclone.adapters.MyPostsAdapter
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.ActivityUserProfileBinding
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.model.UserModel
import com.murilofb.instaclone.ui.home.post.PostActivity

class UserProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserProfileBinding
    lateinit var postOwnerUser: UserModel
    val model: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initUserData()
        initRecycler()
    }

    override fun onDestroy() {
        model.detachListeners()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    private fun initLayout() {
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        //Deactivating follow button whiles isn't already loaded
        with(binding.headerUserProfile.btnActionProfile) {
            setOnClickListener(null)
            text = ""
        }

        setContentView(binding.root)
        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbarUserProfile.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    private fun initUserData() {
        postOwnerUser = intent.getSerializableExtra(IntentTags.EXTRA_USER) as UserModel
        model.setUser(postOwnerUser)
        //Observing changes in the User Profile
        model.user.observe(this, {
            postOwnerUser = it
            //Username
            supportActionBar?.title = it.username
            //ProfilePicture
            Glide.with(this)
                .load(Uri.parse(it.photoLink))
                .placeholder(R.drawable.default_profile_image)
                .into(binding.headerUserProfile.imgProfileImage)
            ////Posts, Followers, Following
            with(binding.headerUserProfile) {
                txtPostsCount.text = it.postsCount.toString()
                txtFollowersCount.text = it.followersCount.toString()
                txtFollowingCount.text = it.followingCount.toString()
            }
        })
        //Observing if the user is or not following the requested user
        model.amIFollowingIt().observe(this, {
            with(binding.headerUserProfile.btnActionProfile) {
                if (it) {
                    text = getString(R.string.unfollow)
                    setOnClickListener { model.unfollowIt() }
                } else {
                    text = getString(R.string.follow)
                    setOnClickListener { model.followIt() }
                }
            }
        })
        //Observing changes in the posts of the user
        model.userPostsList.observe(this, { adapter.updateList(it) })
    }

    val adapter = MyPostsAdapter()
    private fun initRecycler() {
        with(binding.recyclerUserPosts) {
            layoutManager = GridLayoutManager(applicationContext, 3, RecyclerView.VERTICAL, false)
            adapter = this@UserProfileActivity.adapter
        }
        setCustomRecyclerClickListener()
    }

    private fun setCustomRecyclerClickListener() {
        adapter.setOnClickListener {
            val position = binding.recyclerUserPosts.getChildLayoutPosition(it)
            val post = adapter.getItemAtPosition(position)
            val intent = Intent(baseContext, PostActivity::class.java)
            intent.putExtra(IntentTags.EXTRA_USER, postOwnerUser)
            intent.putExtra(IntentTags.EXTRA_POST, post)
            startActivity(intent)
        }
    }

}
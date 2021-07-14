package com.murilofb.instaclone.ui.home.post

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murilofb.instaclone.R
import com.murilofb.instaclone.adapters.AdapterComments
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.ActivityPostBinding
import com.murilofb.instaclone.firebase.RealtimeDatabaseHelper
import com.murilofb.instaclone.helper.ToastHelper
import com.murilofb.instaclone.model.Comment
import com.murilofb.instaclone.model.Post
import com.murilofb.instaclone.model.UserModel

class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostBinding
    lateinit var model: PostViewModel
    lateinit var toast: ToastHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(PostViewModel(application)::class.java)
        toast = ToastHelper(this)
        initViewModel()
        //  initToolbar()
        initLayout()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

/*
    private fun initToolbar() {
        setSupportActionBar(binding.toolbarPost.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = getString(R.string.post_visualization)
    }

 */

    private fun initLayout() {
        loadUserInfo()//Loading the full user data
        loadPostInfo()//With the full post data
        addClickListeners()//Adding click function to layout components
        initRecycler()
    }

    private fun initViewModel() {
        model.setCurrentPost(intent.getSerializableExtra(IntentTags.EXTRA_POST) as Post)
        model.user = intent.getSerializableExtra(IntentTags.EXTRA_USER) as UserModel
        model.loadCommentsList()
        model.observeLikeStatus()
        model.observeChangesInThePost()
        addObservers()//Observing the changes in the ViewModel
    }

    //Adding observers to the viewModel
    private fun addObservers() {
        //Toast message observer
        //Showing a new Toast when a new message is received
        model.getToastMessage().observe(this, { toast.showToast(it) })
        model.getCommentsList().observe(this, { adapter.updateList(it) })
        model.haveILikedIt().observe(this, {
            with(binding.btnLike) {
                if (it) {
                    setOnClickListener { model.unlikeIt() }
                    setImageResource(R.drawable.ic_liked)
                } else {
                    setOnClickListener { model.likeIt() }
                    setImageResource(R.drawable.ic_unliked)
                }
            }
        })
        model.getLikeCount().observe(this, {
            val newText = when (it) {
                0 -> ""
                1 -> "$it ${getString(R.string.single_like)}"
                else -> "$it ${getString(R.string.likes)}"
            }
            binding.txtLikesCount.text = newText
        })
    }

    private fun addClickListeners() {
        //SendButton
        binding.btnSendComment.setOnClickListener {
            val comment = Comment(
                model.post,
                RealtimeDatabaseHelper.getCurrentUser().value,
                binding.edtPostCommentary.text.toString()
            )
            model.publishComment(comment)//it is publishing the comment
            binding.edtPostCommentary.setText("")//Emptying the Edit Text
            // And Closing the inputBox
            val imeOpt = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imeOpt.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    val adapter = AdapterComments()
    private fun initRecycler() {
        with(binding.recyclerComments) {
            adapter = this@PostActivity.adapter
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }
    }

    //Loading both the picture and username into the layout
    private fun loadUserInfo() {
        loadUserPicture()
        binding.postOwnerUserName.text = model.user!!.username
    }

    //Loading Post data into the Layout
    private fun loadPostInfo() {
        loadPostDescription()
        loadPostImage()
    }

    //Loading the description, if is null hides the view
    private fun loadPostDescription() {
        if (model.post!!.description!!.isEmpty()) binding.txtPostDescription.isVisible = false
        else binding.txtPostDescription.text = model.post!!.description
    }

    //Gliding the post image into the layout
    private fun loadPostImage() {
        Glide.with(this)
            .load(Uri.parse(model.post!!.photoLink))
            .placeholder(R.drawable.default_profile_image)
            .into(binding.imgPostImage)
    }

    //Gliding the user profile image into the layout
    private fun loadUserPicture() {
        Glide.with(this)
            .load(Uri.parse(model.user!!.photoLink))
            .placeholder(R.drawable.default_profile_image)
            .into(binding.postOwnerPicture)
    }
}
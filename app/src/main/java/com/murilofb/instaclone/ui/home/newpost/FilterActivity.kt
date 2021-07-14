package com.murilofb.instaclone.ui.home.newpost


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.murilofb.instaclone.R
import com.murilofb.instaclone.adapters.AdapterFilters
import com.murilofb.instaclone.constants.IntentTags
import com.murilofb.instaclone.databinding.ActivityFilterBinding
import com.murilofb.instaclone.helper.ToastHelper
import com.murilofb.instaclone.model.Post
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    private val thumbnailList: MutableList<ThumbnailItem> = ArrayList()
    private lateinit var imageBitmap: Bitmap
    lateinit var model: FilterViewModel
    private var currentFilter: Filter? = null
    private var menuItem: MenuItem? = null
    private lateinit var toast: ToastHelper

    companion object {
        init {
            System.loadLibrary("NativeImageProcessor")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(FilterViewModel(application)::class.java)
        toast = ToastHelper(this)
        initToolbar()
        loadImage()
        initRecycler()
        addObservers()
    }

    private lateinit var dialog: AlertDialog
    private fun showDialog() {
        dialog = AlertDialog.Builder(this)
            .setView(getProgressbar())
            .setTitle(getString(R.string.trying_to_post))
            .setCancelable(false)
            .create()
        dialog.show()
    }


    private fun getProgressbar(): View {
        /*
        val progress = CircularProgressIndicator(this)
        //Configuring the layout params
        val lm = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lm.gravity = Gravity.CENTER
        lm.setMargins(50, 50, 50, 50)
        progress.layoutParams = lm
        progress.isIndeterminate = true
         */
        val progress = layoutInflater.inflate(R.layout.dialog_new_post, null)
        return progress
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_post, menu)
        menuItem = menu?.getItem(0)
        togglePublishFunction(true)
        return super.onCreateOptionsMenu(menu)
    }

    private fun addObservers() {
        model.getToastMessage().observe(this, { toast.showToast(it) })
        model.isTryingToPost().observe(this, {
            togglePublishFunction(!it)
            if (it) showDialog()
            else {
                if (dialog.isShowing) dialog.dismiss()
            }
        })
        model.isPostSuccessful().observe(this, { finish() })
    }

    private fun togglePublishFunction(bool: Boolean) {
        if (bool) {
            menuItem?.setOnMenuItemClickListener {
                val post = Post()
                when (currentFilter) {
                    null -> post.imageBitmap = imageBitmap
                    else -> {
                        val bitmapCopy = imageBitmap.copy(imageBitmap.config, true)
                        post.imageBitmap = currentFilter!!.processFilter(bitmapCopy)
                    }
                }
                post.description = binding.edtTextPostDescription.text.toString()
                model.postPhoto(post)
                false
            }
        } else {
            menuItem?.setOnMenuItemClickListener(null)
        }
    }


    private fun initToolbar() {
        setSupportActionBar(binding.toolbarNewPostFilter.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    private fun initRecycler() {
        loadFilters()
        binding.recyclerFilters.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL,
                false
            )
        binding.recyclerFilters.adapter = configAdapter()
        binding.recyclerFilters.isNestedScrollingEnabled = false
    }

    private fun configAdapter(): AdapterFilters {
        val adapter = AdapterFilters(thumbnailList)
        adapter.setHasStableIds(true)
        adapter.getFilterSelected().observe(this, {
            applyFilter(it)
        })
        return adapter
    }


    private fun loadImage() {
        val imageUri = Uri.parse(intent.getStringExtra(IntentTags.EXTRA_IMAGE_URI))
        imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        binding.newPostImage.setImageBitmap(imageBitmap)
    }

    private fun loadFilters() {
        thumbnailList.clear()
        ThumbnailsManager.clearThumbs()
        val filterList = FilterPack.getFilterPack(this)

        val item = ThumbnailItem()
        item.image = imageBitmap
        item.filterName = getString(R.string.default_filter)
        ThumbnailsManager.addThumb(item)

        for (filter in filterList) {
            val item = ThumbnailItem()
            item.image = imageBitmap
            item.filter = filter
            item.filterName = filter.name
            ThumbnailsManager.addThumb(item)
        }
        thumbnailList.addAll(ThumbnailsManager.processThumbs(applicationContext))
    }

    private fun applyFilter(filter: Filter) {
        currentFilter = filter
        val bitmapCopy = imageBitmap.copy(imageBitmap.config, true)
        if (filter == null) {
            binding.newPostImage.setImageBitmap(imageBitmap)
        } else {
            val filteredImage = filter.processFilter(bitmapCopy)
            binding.newPostImage.setImageBitmap(filteredImage)
        }

    }

}
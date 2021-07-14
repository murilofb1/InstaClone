package com.murilofb.instaclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.murilofb.instaclone.databinding.RecyclerFiltersBinding
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem

class AdapterFilters(private val thumbsList: List<ThumbnailItem> = ArrayList()) :
    RecyclerView.Adapter<AdapterFilters.FiltersViewHolder>() {
    private val filterSelected = MutableLiveData<Filter>()
    private lateinit var binding: RecyclerFiltersBinding

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersViewHolder {
        binding = RecyclerFiltersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FiltersViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FiltersViewHolder, position: Int) {
        val thumb = thumbsList[position]
        binding.txtFilterName.text = thumb.filterName
        binding.imgFilterThumb.setImageBitmap(thumb.image)
        binding.itemLayout.setOnClickListener {
            filterSelected.value = thumb.filter
        }
    }

    override fun getItemCount(): Int = thumbsList.size

    fun getFilterSelected(): LiveData<Filter> = filterSelected

    class FiltersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
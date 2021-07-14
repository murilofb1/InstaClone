package com.murilofb.instaclone.ui.home.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.murilofb.instaclone.adapters.FeedAdapter
import com.murilofb.instaclone.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    companion object {
        val getInstance = FeedFragment()
    }

    private lateinit var binding: FragmentFeedBinding
    private val model: FeedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        initRecycler()
        return binding.root
    }

    private fun initRecycler() {
        val feedAdapter = FeedAdapter(requireContext())
        model.loadPosts()
        model.loadLikes()
        model.getPostsList().observe(viewLifecycleOwner, { feedAdapter.updatePostsList(it) })
        model.getLikesList().observe(viewLifecycleOwner, { feedAdapter.updateLikesList(it) })
        with(binding.rvFeed) {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

    }


}
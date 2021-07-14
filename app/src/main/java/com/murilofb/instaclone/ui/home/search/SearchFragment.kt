package com.murilofb.instaclone.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.murilofb.instaclone.adapters.AdapterUsersList
import com.murilofb.instaclone.R
import com.murilofb.instaclone.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    companion object {
        val getInstance = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: AdapterUsersList
    val model: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.inflate(inflater)
        initSearchView()
        initRecycler()
        return binding.root
    }

    private fun initSearchView() {
        binding.usersSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                model.searchForUser(newText)
                return true
            }

        })
    }

    private fun initRecycler() {
        adapter = AdapterUsersList(requireActivity())
        model.getUsersList().observe(viewLifecycleOwner, { adapter.updateList(it) })
        with(binding.recyclerUsersSearchResult) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            adapter = this@SearchFragment.adapter
        }
    }
}
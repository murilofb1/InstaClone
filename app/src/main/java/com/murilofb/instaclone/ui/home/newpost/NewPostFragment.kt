package com.murilofb.instaclone.ui.home.newpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.murilofb.instaclone.databinding.FragmentNewPostBinding
import com.murilofb.instaclone.helper.MyLifeCycleObserver

class NewPostFragment : Fragment() {
    companion object {
        val getInstance = NewPostFragment()
    }

    private lateinit var binding: FragmentNewPostBinding
    private lateinit var observer: MyLifeCycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = MyLifeCycleObserver(activity as AppCompatActivity)
        lifecycle.addObserver(observer)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding.btnNewCameraPhoto.setOnClickListener {
            observer.pickImageFromCamera()
        }
        binding.btnNewGalleryPhoto.setOnClickListener {
            observer.pickImageFromGallery()
        }
        return binding.root
    }

}
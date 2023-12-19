package com.example.lab06

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

class GallerySwipeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_swipe, container, false)

        val vpAdapter = GallerySwipeAdapter(requireActivity(), requireContext())

        val viewPager = view.findViewById<ViewPager2>(R.id.gallerySwipePager)
        viewPager.adapter = vpAdapter

        return view
    }


}
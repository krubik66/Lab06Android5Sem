package com.example.lab06

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

class GallerySwipeFragment : Fragment() {
    lateinit var viewPager: ViewPager2
    var position: Int = 0

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

        viewPager = view.findViewById<ViewPager2>(R.id.gallerySwipePager)
        viewPager.adapter = vpAdapter
        viewPager.currentItem = position

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener("msgtoimg", viewLifecycleOwner){
                _, bundle ->
            run {
                position = bundle.getInt("position")
            }
            viewPager.currentItem = position
        }
    }
}
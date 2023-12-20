package com.example.lab06

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.coroutines.coroutineContext

class GallerySwipeAdapter(fa: FragmentActivity, context: Context, startPos: Int): FragmentStateAdapter(fa) {

    companion object {
        var PAGE_COUNT = Int.MAX_VALUE // Replace this with the actual number of tabs/pages
    }


    val imageList: MutableList<GalleryItem> = ImageRepo.getInstance(context).getSharedList()!!

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        // Return the proper fragment for each position value
        if (position < imageList.size) {
            return GallerySwipeTabFragment.newInstance(imageList[position].curi!!)
        }
        return GallerySwipeTabFragment.newInstance(imageList[0].curi!!)
    }
}
package com.example.lab06

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.coroutines.coroutineContext

class GallerySwipeAdapter(fa: FragmentActivity, context: Context): FragmentStateAdapter(fa) {

    companion object {
        var PAGE_COUNT = Int.MAX_VALUE // Replace this with the actual number of tabs/pages
    }

    val ctx: Context = context
    var imageList: MutableList<GalleryItem> = ImageRepo.getInstance(context).getCurrentList()

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        imageList = ImageRepo.getInstance(ctx).getCurrentList()
        // Return the proper fragment for each position value
        if (position < imageList.size) {
            return GallerySwipeTabFragment.newInstance(imageList[position].curi!!)
        }
        return GallerySwipeTabFragment.newInstance(imageList[0].curi!!)
    }
}
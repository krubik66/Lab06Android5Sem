package com.example.lab06

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab06.databinding.FragmentGalleryImagesBinding
import com.example.lab06.databinding.FragmentThirdBinding
import com.example.lab06.databinding.GalleryItemBinding
import java.io.FileNotFoundException
import java.io.InputStream


class GalleryImagesFragment : Fragment() {

    private lateinit var adapter: PhotoListAdapter
    private lateinit var _binding: FragmentGalleryImagesBinding

    lateinit var imageRepo: ImageRepo
    lateinit var imageList: MutableList<GalleryItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryImagesBinding.inflate(inflater, container, false)

        val recView = _binding.recyclerView
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        imageRepo = ImageRepo.getInstance(requireContext())
        imageList = imageRepo.getSharedList()!!

        adapter = PhotoListAdapter(requireContext(), imageList)
        recView.adapter = adapter
        _binding.addPhotoButton.setOnClickListener {
            openCamera()
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallerymenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.cameraButton -> {
                openCamera()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openCamera() {

    }

    private val DiffCallback = object : DiffUtil.ItemCallback<GalleryItem>() {
        override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
            return oldItem == newItem
        }
    }

    inner class PhotoListAdapter(val appContext: Context,
                           val galleryList: MutableList<GalleryItem>):
        ListAdapter<GalleryItem, PhotoListAdapter.MyViewHolder>(DiffCallback) {
        inner class MyViewHolder(viewBinding: GalleryItemBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {
            val img: ImageView = viewBinding.imageGalleryView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val viewBinding = GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            return MyViewHolder(viewBinding)
        }

        override fun getItemCount(): Int {
            return galleryList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            var currData = galleryList[position]
            holder.itemView.setOnClickListener {
                findNavController().navigate(R.id.action_galleryImagesFragment_to_gallerySwipeFragment)
            }
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                currData.curi?.let {
                    holder.img.setImageBitmap(appContext.contentResolver.
                    loadThumbnail(it, Size(72, 72), null))
                }
            }
            else holder.img.setImageBitmap(getBitmapFromUri(appContext, currData.curi))
        }

        fun getBitmapFromUri(con: Context, uri: Uri?): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                val image_stream: InputStream
                try {
                    image_stream = uri?.let {
                        con.contentResolver.openInputStream(it)
                    }!!
                    bitmap = BitmapFactory.decodeStream(image_stream)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryImagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryImagesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
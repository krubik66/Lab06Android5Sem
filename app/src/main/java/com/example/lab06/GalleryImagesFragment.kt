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
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab06.databinding.FragmentGalleryImagesBinding
import com.example.lab06.databinding.GalleryItemBinding
import java.io.FileNotFoundException
import java.io.InputStream


class GalleryImagesFragment : Fragment() {

    private lateinit var adapter: PhotoListAdapter
    private lateinit var _binding: FragmentGalleryImagesBinding

    lateinit var imageRepo: ImageRepo
    lateinit var imageList: MutableList<GalleryItem>
    lateinit var recView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        setAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryImagesBinding.inflate(inflater, container, false)

        recView = _binding.recyclerView
        recView.layoutManager = GridLayoutManager(requireContext(), 2)
        imageRepo = ImageRepo.getInstance(requireContext())
        setAdapter()
        _binding.addPhotoButton.setOnClickListener {
            openCamera()
        }

        return _binding.root
    }

    fun setAdapter() {
        when(imageRepo.getStorage()) {
            imageRepo.SHARED_S -> imageList = imageRepo.getSharedList()!!
            imageRepo.PRIVATE_S -> imageList = imageRepo.getAppList()!!
        }
//        Toast.makeText(requireContext(), imageList[0].name, Toast.LENGTH_LONG).show()

        adapter = PhotoListAdapter(requireContext(), imageList)
        recView.adapter = adapter
//        Toast.makeText(requireContext(), adapter.itemCount, Toast.LENGTH_LONG).show()
        adapter.notifyDataSetChanged()
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
            R.id.dataButton -> {
                imageRepo.changeStorage()
                setAdapter()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun openCamera() {
        findNavController().navigate(R.id.action_galleryImagesFragment_to_addImageFragment)
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
            val textView: TextView = viewBinding.textViewForImage
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
                parentFragmentManager.setFragmentResult(
                    "msgtoimg", bundleOf(
                        "position" to position
                    )
                )
                findNavController().navigate(R.id.action_galleryImagesFragment_to_gallerySwipeFragment)
            }
            holder.textView.text = currData.name
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                currData.curi?.let {
                    holder.img.setImageBitmap(appContext.contentResolver.
                    loadThumbnail(it, Size(72, 72), null))
                }
            }
            else holder.img.setImageBitmap(getBitmapFromUri(appContext, currData.curi))
//            loadImageWithReducedQuality(appContext, currData.curi!!, holder.img)
        }

        fun loadImageWithReducedQuality(context: Context, uri: Uri, imageView: ImageView) {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

            inputStream?.use {
                // Decode the input stream into a Bitmap
                val originalBitmap = BitmapFactory.decodeStream(it)

                // Adjust the compression quality (0-100)
                val compressionQuality = 60
                val compressedBitmap = compressBitmap(originalBitmap, compressionQuality)

                // Set the compressed Bitmap into the ImageView
                imageView.setImageBitmap(compressedBitmap)
            }
        }

        private fun compressBitmap(originalBitmap: Bitmap, quality: Int = 50): Bitmap {
            val outputStream = java.io.ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val compressedByteArray = outputStream.toByteArray()
            return BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
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
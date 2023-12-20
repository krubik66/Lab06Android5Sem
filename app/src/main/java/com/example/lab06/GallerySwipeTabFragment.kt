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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.lab06.databinding.FragmentGallerySwipeTabBinding
import java.io.FileNotFoundException
import java.io.InputStream

class GallerySwipeTabFragment : Fragment() {

    lateinit var image: ImageView
    var oferredUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            oferredUri = it.getParcelable("uri")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentGallerySwipeTabBinding.inflate(inflater, container, false)

        image = binding.imageInSwipeView
        binding.buttonSaveInGallerySwipe.setOnClickListener {
            val data = requireActivity().getSharedPreferences("additional", Context.MODE_PRIVATE)
            val editor = data.edit()

            editor.putString("uri", oferredUri.toString())
            editor.putBoolean("new", true)

            editor.apply()
            findNavController().navigate(R.id.action_gallerySwipeFragment_to_firstFragment)
        }
        loadImgFromCuri(oferredUri, image)
        return binding.root
    }

    fun loadImgFromCuri(curi: Uri?, img: ImageView) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            curi?.let {
                img.setImageBitmap(requireContext().contentResolver.
                loadThumbnail(it, Size(72, 72), null))
            }
        }
        else img.setImageBitmap(getBitmapFromUri(requireContext(), curi))
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

    companion object {
        @JvmStatic
        fun newInstance(curi: Uri) =
            GallerySwipeTabFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("uri", curi)
                }
            }
    }
}
package com.example.lab06

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.example.lab06.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var _binding: FragmentStartBinding
    private var basePhotoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater, container, false)


        val dir2 = Environment.getExternalStorageDirectory()
        val dir3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir4 = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        _binding . tv1path . text = basePhotoUri . scheme +":/" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI.path
        _binding . tv2path . text = dir2 . absolutePath
        _binding.tv3path.text = dir3.absolutePath
        _binding . tv4path . text = dir4 ?. absolutePath ?: "nothing"
        dir4?.let {
            val theuri = FileProvider.getUriForFile(requireContext(),
                "${context?.packageName}.provider", it)
            _binding . tv5path . text = theuri . scheme +":/" + theuri.path
        }



        return _binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
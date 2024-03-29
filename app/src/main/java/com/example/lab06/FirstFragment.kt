package com.example.lab06
import android.content.Context
import android.content.SharedPreferences
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
import android.widget.TextView
import java.io.FileNotFoundException
import java.io.InputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var beforeImage: TextView
    lateinit var afterImage1: TextView
    lateinit var afterImage2: TextView
    lateinit var imageMain: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_first, container, false)

        beforeImage = view.findViewById(R.id.beforeImage)
        afterImage1 = view.findViewById(R.id.afterImage1)
        afterImage2 = view.findViewById(R.id.afterImage2)
        imageMain = view.findViewById(R.id.mainImage)

        return view
    }

    fun applyData(){
        val data: SharedPreferences = requireActivity().getSharedPreferences("L6_preferences", Context.MODE_PRIVATE)
        beforeImage.text = data.getString("beforeImage", "I am on the top!")
        afterImage1.text = data.getString("afterImage1", "afterImage1")
        afterImage2.text = data.getString("afterImage2", "afterImage2")
        val data2: SharedPreferences = requireActivity().getSharedPreferences("additional", Context.MODE_PRIVATE)
        if(data2.getBoolean("new", false)) {
            loadImgFromCuri(Uri.parse(data2.getString("uri", "")), imageMain)
        }
        else {
            when (data2.getInt("imageNumber", -1)) {
                1 -> imageMain.setImageResource(R.drawable.playericon)
                2 -> imageMain.setImageResource(R.drawable.skeletonicon)
                3 -> imageMain.setImageResource(R.drawable.orcicon)
                else -> imageMain.setImageResource(R.drawable.homeicon)
            }
        }
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyData()
    }

    override fun onResume() {
        super.onResume()
        applyData()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.ar.parcialtp3.fragments

import ImageCardAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R

class PhotoSelectionFragment : Fragment() {
    lateinit var v: View

    lateinit var photoRecyclerView: RecyclerView
    lateinit var imageCardAdapter: ImageCardAdapter
    lateinit var btnUpload: Button


    val testRecycler = arrayListOf("https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_10263.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_10715.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_10822.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1128.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1145.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_115.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1150.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_11570.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_11584.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1186.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_11953.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1234.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_1259.jpg",
        "https://images.dog.ceo/breeds/hound-afghan/n02088094_12664.jpg",)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_photo_selection, container, false)


        photoRecyclerView = v.findViewById(R.id.rec_imageCard_list)
        photoRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        imageCardAdapter = ImageCardAdapter(testRecycler)
        photoRecyclerView.adapter = imageCardAdapter
        btnUpload = v.findViewById(R.id.btnUploadPhotos)
        return v
    }

    override fun onStart() {
        super.onStart()
        btnUpload.setOnClickListener {
            val selectedPhotos = imageCardAdapter.getSelectedPhotos()
            Log.d("selecte dphotos", selectedPhotos.toString())

        }
    }
}
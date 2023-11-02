package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.ImageAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

class DetailFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail, container, false)
        //TODO lateinit var para db
        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
            }
        })
        return v
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 10000)
    }

    private fun init() {
        viewPager2 = v.findViewById(R.id.imgCarrousel)
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        val documentId = "KPuDeYxakHBkM91fjQ7D"

        db.collection("Publications").document(documentId).collection("dog")
            .get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result!!) {
                        // Access the array of images inside the 'dogs' collection
                        val imagesArray = document.getString("images") as? ArrayList<String>
                        if (imagesArray != null) {
                            // Iterate through the imagesArray and do something with the images
                            for (i in 0 until imagesArray.size) {
                                imageList.add(imagesArray[i].toInt())
                                Log.d("Image URL", imagesArray[i])
                            }
                        } else {
                            // Handle the case where 'images' field is not present or not an ArrayList<String>
                            Log.e("Error", "Images field not found or not of the correct type")
                        }
                    }
                } else {
                    // Handle errors
                    Log.e("Error", "Error getting documents: ${querySnapshot.exception}")
                }
            }

        adapter = ImageAdapter(imageList, viewPager2)
        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}
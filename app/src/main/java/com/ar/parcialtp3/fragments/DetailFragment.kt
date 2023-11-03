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
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.services.firebase.GetPublicationsService
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

class DetailFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<String>
    private lateinit var adapter: ImageAdapter
    private val getPublicationsService = GetPublicationsService()

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
         val publicationId = DetailFragmentArgs.fromBundle(requireArguments()).publicationId
        Log.d("publicationId", publicationId)
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 10000)
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

        getPublicationsService.getPublicationById(documentId) { document, exception ->
            if (exception == null) {
                if (document != null) {
                    val publication = document.toObject(PublicationEntity::class.java)
                    if (publication != null) {
                        imageList.clear()
                        for (i in 0 until publication.dog.images.size) {
                            imageList.add(publication.dog.images[i])
                        }

                        adapter = ImageAdapter(imageList, viewPager2)
                        viewPager2.adapter = adapter
                        viewPager2.offscreenPageLimit = 3
                        viewPager2.clipToPadding = false
                        viewPager2.clipChildren = false
                        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                    }
                }
            } else {
                Log.d("asd", "No hay publications")
            }
        }
    }
}

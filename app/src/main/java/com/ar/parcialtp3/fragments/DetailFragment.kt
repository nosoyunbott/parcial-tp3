package com.ar.parcialtp3.fragments

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

class DetailFragment : Fragment() {

    private lateinit var v: View
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<String>
    private lateinit var adapter: ImageAdapter
    private val getPublicationsService = GetPublicationsService()

    lateinit var txtDetailName: TextView
    lateinit var txtDetailLocation: TextView
    lateinit var txtDetailEdad: TextView
    lateinit var txtDetailGender: TextView
    lateinit var txtDetailWeight: TextView

    lateinit var imgDetailOwner: ImageView
    lateinit var txtDetailUsername: TextView
    lateinit var imgDetailPhone: ImageView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail, container, false)
        //TODO lateinit var para db
        txtDetailName = v.findViewById(R.id.txtDetailName)
        txtDetailLocation = v.findViewById(R.id.txtDetailLocation)
        txtDetailEdad = v.findViewById(R.id.txtDetailEdad)
        txtDetailGender = v.findViewById(R.id.txtDetailGender)
        txtDetailWeight = v.findViewById(R.id.txtDetailWeight)
        imgDetailOwner = v.findViewById(R.id.imgDetailOwner)
        txtDetailUsername = v.findViewById(R.id.txtDetailUsername)
        imgDetailPhone = v.findViewById(R.id.imgDetailPhone)
        //
        init()
        setUpTransformer()
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

        val documentId = "4w5MKWmy3tQ9pDE95Etg"
        //TODO cambiar id y traer de la card

        getPublicationsService.getPublicationById(documentId) { document, exception ->
            if (exception == null) {
                if (document != null) {
                    val publication = document.toObject(PublicationEntity::class.java)
                    if (publication != null) {
                        //Img carrousel
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

                        //Dog data
                        txtDetailName.text = publication.dog.name
                        txtDetailLocation.text = publication.location
                        txtDetailEdad.text = publication.dog.age.toString()

                        //Dog squares
                        txtDetailGender.text = publication.dog.sex
                        txtDetailWeight.text = "${publication.dog.weight}kg"

                        //User data
                        Glide.with(this).load(publication.owner.image).into(imgDetailOwner)
                        txtDetailUsername.text = publication.owner.name
                        //Phone to phone app
                        imgDetailPhone.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${publication.owner.phone}")
                            startActivity(intent)
                        }
                    }
                }
            } else {
                Log.d("ErrorPublications", "No hay publications")
            }
        }
    }
}

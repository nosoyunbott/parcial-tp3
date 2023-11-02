package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.ar.parcialtp3.R
import com.ar.parcialtp3.domain.Dog
import com.ar.parcialtp3.domain.Owner
import com.ar.parcialtp3.domain.Provinces
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.services.firebase.GetPublicationsService
import com.ar.parcialtp3.services.firebase.SavePublicationService

class HomeFragment : Fragment() {

    lateinit var v: View

    val savePublicationsService = SavePublicationService()

    val getPublicationsService = GetPublicationsService()

    var publicationList: MutableList<PublicationEntity> = ArrayList()
    lateinit var filterContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)

        return v
    }

    override fun onStart() {
        super.onStart()

        // EJEMPLO LLAMADO A LA BASE POR EL SERVICE
        //----------------------------------------------------------------------------------
        getPublicationsService.getPublications(false) { documents, exception ->
            if (exception == null) {
                if (documents != null) {
                    for(d in documents){
                        Log.d("asd", d.getString("name").toString())
                        Log.d("asd", d.getBoolean("isAdopted").toString())
                        Log.d("id", d.id)
                    }
                }
            } else {
                Log.d("asd", "No hay publications")
            }
        }
        //val listImages = arrayListOf("https://images.dog.ceo/breeds/setter-irish/n02100877_123.jpg")
        //val dog = Dog("BoyOlmi",15,"Macho", "Caniche", "Mini", listImages, false)
        //val owner = Owner("Tom", 1156943023, "https://images.dog.ceo/breeds/hound-afghan/n02088094_8063.jpg")
        //val publication = PublicationEntity(dog, owner, Provinces().getList()[5],"Perrito lindo encontrado en la ruta")
        //savePublicationsService.savePublication(publication)


    }

    fun refreshRecyclerView() {
        val razas = listOf("Golden", "Caniche", "Pastor")
        for (filterName in razas) {
            val btnFilter = Button(context)
            btnFilter.text = filterName
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(10, 5, 10, 0)
            btnFilter.layoutParams = layoutParams
            btnFilter.textSize = 16F
            btnFilter.background = resources.getDrawable(R.drawable.rounded_violet_background)

            btnFilter.setOnClickListener {
                val filter = btnFilter.text.toString()
                val filteredList =
                    publicationList.filter { it.dog.breed == filter } as MutableList
                //requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                //recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }
}
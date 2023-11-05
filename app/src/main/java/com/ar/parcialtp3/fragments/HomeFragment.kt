package com.ar.parcialtp3.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.services.firebase.FirebaseService
import com.ar.parcialtp3.utils.SharedPrefUtils

class HomeFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    val firebaseService = FirebaseService()
    lateinit var filterContainer: LinearLayout
    lateinit var favouriteBtn: ImageButton
    lateinit var recCardList: RecyclerView
    lateinit var cardRecycler: View
    private lateinit var linearLayoutManager: LinearLayoutManager
    var cardList: MutableList<Card> = ArrayList()
    private lateinit var cardListAdapter: CardAdapter

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)


        recCardList = v.findViewById(R.id.cardRecyclerView)
        filterContainer = v.findViewById(R.id.filterContainer)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)

        cardRecycler = inflater.inflate(R.layout.card_recycler, container, false)
        favouriteBtn = cardRecycler.findViewById(R.id.favouriteBtn)

        val fragmentContainer = v.findViewById<LinearLayout>(R.id.linearLayoutHome)
        fragmentContainer.addView(cardRecycler)




        Log.d("BOTON", favouriteBtn.toString())

        return v
    }

    override fun onStart() {
        super.onStart()
        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager
        cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id ->
            SharedPrefUtils().setFavouritePublication(id, requireContext())
        })
        recCardList.adapter = cardListAdapter
        refreshRecyclerView()

        firebaseService.getPublications(false) { documents, exception ->
            if (exception == null) {
                if (documents != null) {
                    for (d in documents) {
                        val publication = d.toObject(PublicationEntity::class.java)
                        if (publication != null) {
                            val dog = Card(
                                publication.dog.name,
                                publication.dog.breed,
                                publication.dog.subBreed,
                                publication.dog.age,
                                publication.dog.sex,
                                d.id
                            )
                            cardList.add(dog)
                        }
                    }
                }
            } else {
                Log.d("asd", "No hay publications")
            }
            cardListAdapter.notifyDataSetChanged()
        }


    }


    fun setPublicationAsFavourite(position: Int) {
        Log.d("position", position.toString())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun refreshRecyclerView() {
        val razas = listOf("Golden", "Caniche", "Salchicha")
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
                    cardList.filter { it.breed == filter } as MutableList
                cardListAdapter =
                    CardAdapter(filteredList, this@HomeFragment, onClickFavourite = { position ->
                        Log.d("CLICK", position.toString())

                    })
                recCardList.adapter = cardListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }

    override fun onViewItemDetail(card: Card) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment2(card.id)
        val navController = v.findNavController()
        navController.navigate(action)
    }


}
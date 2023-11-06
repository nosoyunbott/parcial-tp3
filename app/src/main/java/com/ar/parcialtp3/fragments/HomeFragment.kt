package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.domain.Provinces
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.services.firebase.FirebaseService
import com.ar.parcialtp3.utils.SharedPrefUtils
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View


    private val firebaseService = FirebaseService()
    private lateinit var publications: List<PublicationEntity>

    private lateinit var filterContainer: LinearLayout

    private lateinit var recCardList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var cardList: MutableList<Card> = ArrayList()
    private lateinit var cardListAdapter: CardAdapter

    private lateinit var moreFiltersTextView: TextView
    private lateinit var clearFiltersTextView: TextView
    private lateinit var createDateTextView: TextView
    private lateinit var provinceTextView: TextView

    private lateinit var breedFilter : String
    private lateinit var provinceFilter : String
    private lateinit var filteredList : MutableList<Card>
    private var isAsc = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        recCardList = v.findViewById(R.id.cardRecyclerView)
        filterContainer = v.findViewById(R.id.filterContainer)
        moreFiltersTextView = v.findViewById(R.id.moreFiltersTextView)
        clearFiltersTextView = v.findViewById(R.id.clearFiltersTextView)
        createDateTextView = v.findViewById(R.id.createDateTextView)
        provinceTextView = v.findViewById(R.id.provinceTextView)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)

        return v
    }

    override fun onStart() {
        super.onStart()
        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager

        provinceFilter = ""
        breedFilter = ""
        filteredList = mutableListOf()

        cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, _ ->
            SharedPrefUtils(requireContext()).toggleFavorite(id)
            val itemOnList = cardList.indexOfFirst { it.id == id }
            cardListAdapter.notifyItemChanged(itemOnList)

        })
        recCardList.adapter = cardListAdapter

        cardList.clear()

        firebaseService.getPublications(false) { documents, exception ->
            if (exception == null) {
                if (documents != null) {
                    publications =
                        documents.mapNotNull { it.toObject(PublicationEntity::class.java) }
                    for (d in documents) {
                        val publication = d.toObject(PublicationEntity::class.java)
                        if (publication != null) {
                            val dog = Card(
                                publication.dog.name,
                                publication.dog.breed,
                                publication.dog.subBreed,
                                publication.dog.age,
                                publication.dog.sex,
                                d.id,
                                publication.location,
                                publication.dog.adopted,
                                d.getDate("timestamp")!!
                            )
                            Log.d("Alo",dog.createDate.toString())
                            cardList.add(dog)
                        }
                    }
                }
            } else {
                Snackbar.make(v, "No hay publicaciones disponibles", Snackbar.LENGTH_SHORT).show()
            }
            cardListAdapter.notifyDataSetChanged()
            refreshRecyclerView()
        }
        moreFiltersTextView.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            val provincesArray = Provinces().getList()

            for (province in provincesArray) {
                popupMenu.menu.add(province)
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                provinceFilter = menuItem.title.toString()
                provinceTextView.text = provinceFilter
                provinceTextView.visibility = View.VISIBLE
                filterCardList(provinceFilter, breedFilter)
                true
            }

            popupMenu.show()
        }

        createDateTextView.setOnClickListener{
            orderCardList()
        }
    }

    private fun refreshRecyclerView() {

        var selectedButton: Button? = null

        val breeds = mutableSetOf<String>()
        for (p in publications) {
            breeds.add(p.dog.breed)
        }
        for (filterName in breeds) {
            val btnFilter = Button(context)
            val spannableString = SpannableString("   $filterName")
            val drawable = context?.resources?.getDrawable(R.drawable.fingerprint)

            if (drawable != null) {
                val imageWidthInPixels = 60
                val imageHeightInPixels = 60

                drawable.setBounds(0, 0, imageWidthInPixels, imageHeightInPixels)
            }

            val imageSpan = drawable?.let { ImageSpan(it, ImageSpan.ALIGN_BASELINE) }
            spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            btnFilter.text = spannableString
            btnFilter.textSize = 16F


            btnFilter.setBackgroundResource(R.drawable.button_transparent)

            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(30, 5, 10, 0)
            btnFilter.layoutParams = layoutParams

            btnFilter.setOnClickListener {
                breedFilter = filterName
                filterCardList(provinceFilter, breedFilter)

                selectedButton?.setBackgroundResource(R.drawable.button_transparent)

                btnFilter.setBackgroundResource(R.drawable.rounded_violet_background_big_radius)

                selectedButton = btnFilter
            }

            clearFiltersTextView.setOnClickListener {
                provinceFilter = ""
                breedFilter = ""
                selectedButton?.setBackgroundResource(R.drawable.button_transparent)
                cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, position -> })
                recCardList.adapter = cardListAdapter
                filteredList = cardList.toMutableList()
                provinceTextView.visibility = View.GONE
            }

            filterContainer.addView(btnFilter)
        }
    }

    private fun filterCardList(province: String, breed: String) {
        filteredList = cardList.toMutableList()
        if(province.isNotEmpty()){
            filteredList = filteredList.filter { it.location == province } as MutableList
            cardListAdapter = CardAdapter(filteredList, this, onClickFavourite = { id, position -> })
            recCardList.adapter = cardListAdapter
        }
        if(breed.isNotEmpty()){
            filteredList = filteredList.filter { it.breed == breed } as MutableList
            cardListAdapter = CardAdapter(filteredList, this, onClickFavourite = { id, position -> })
            recCardList.adapter = cardListAdapter
        }
    }

    override fun onViewItemDetail(card: Card) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment2(card.id)
        val navController = v.findNavController()
        navController.navigate(action)
    }

    private fun orderCardList(){
        if(filteredList.isEmpty()){
            filteredList = cardList.toMutableList()
        }
        if(isAsc){
            filteredList.sortBy { it.createDate }
            isAsc = false
        }else{
            filteredList.sortByDescending { it.createDate }
            isAsc = true
        }
        cardListAdapter = CardAdapter(filteredList, this, onClickFavourite = { id, position -> })
        recCardList.adapter = cardListAdapter
        cardListAdapter.notifyDataSetChanged()
    }
}
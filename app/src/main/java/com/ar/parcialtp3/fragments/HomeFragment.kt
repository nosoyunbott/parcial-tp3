package com.ar.parcialtp3.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.domain.Breed
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.domain.Provinces
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.services.DogDataService
import com.ar.parcialtp3.services.firebase.FirebaseService
import com.ar.parcialtp3.utils.SharedPrefUtils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar



class HomeFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View

    private val firebaseService = FirebaseService()
    private lateinit var publications: List<PublicationEntity>
    private lateinit var searchEditText: AutoCompleteTextView
    private lateinit var suggestionAdapter: ArrayAdapter<String>
    private lateinit var dogServiceAPI: DogDataService
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var filterContainer: LinearLayout
    private lateinit var subBreedSet:MutableSet<String>
    private lateinit var breedSet:MutableSet<String>

    private lateinit var recCardList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var cardList: MutableList<Card> = ArrayList()
    private lateinit var cardListAdapter: CardAdapter
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var moreFiltersTextView: TextView
    private lateinit var clearFiltersTextView: TextView
    private lateinit var createDateTextView: TextView
    private lateinit var provinceTextView: TextView

    private lateinit var breedFilter : String
    private lateinit var subBreedFilter : String
    private lateinit var provinceFilter : String
    private lateinit var filteredList : MutableList<Card>
    private var isAsc = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)
        dogServiceAPI= DogDataService()
        recCardList = v.findViewById(R.id.cardRecyclerView)
        filterContainer = v.findViewById(R.id.filterContainer)

        sharedPreferences =
            requireContext().getSharedPreferences("search_suggestions", Context.MODE_PRIVATE)

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
        subBreedSet=mutableSetOf()
        breedSet=mutableSetOf()


        provinceFilter = ""
        breedFilter = ""
        subBreedFilter=""
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
                                d.getDate("createDate")!!
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
                filterCardList(provinceFilter, breedFilter,subBreedFilter)
                true
            }

            popupMenu.show()
        }

        createDateTextView.setOnClickListener{
            orderCardList()
        }

        searchEditText = v.findViewById(R.id.searchEditText)


        suggestionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        searchEditText.setAdapter(suggestionAdapter)



        searchEditText.setOnItemClickListener { parent, view, position, id ->
            filteredList.clear()

            val selectedSuggestion = parent.getItemAtPosition(position).toString()
            Log.d("breedSet",breedSet.toString())
            if(breedSet.contains(selectedSuggestion))
            {
                breedFilter=selectedSuggestion.toUpperCase();
            }
            else
            {
                subBreedFilter=selectedSuggestion.toUpperCase();
            }

            filterCardList(provinceFilter,breedFilter,subBreedFilter)
        }




        searchEditText.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {
                handler.postDelayed({  lifecycleScope.launch {
                    fetchSuggestions()
                }},500)

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return;
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return;
            }
        })
    }



    private suspend fun fetchSuggestions() {

            suggestionAdapter.clear()
            suggestionAdapter.notifyDataSetChanged()
           val breeds:List<Breed> = dogServiceAPI.getAllBreeds()
           val allBreedSet = mutableSetOf<String>()
            for (breed in breeds) {
                allBreedSet.add(breed.name)
                allBreedSet.addAll(breed.subBreeds)
                breedSet.add(breed.name)
                subBreedSet.addAll(breed.subBreeds)

            }

            val allBreedList = allBreedSet.toList()

            suggestionAdapter.clear()
            suggestionAdapter.addAll(allBreedList)
            suggestionAdapter.notifyDataSetChanged()

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
                filterCardList(provinceFilter, breedFilter,subBreedFilter)

                selectedButton?.setBackgroundResource(R.drawable.button_transparent)

                btnFilter.setBackgroundResource(R.drawable.rounded_violet_background_big_radius)

                selectedButton = btnFilter
            }

            clearFiltersTextView.setOnClickListener {
                provinceFilter = ""
                breedFilter = ""
                subBreedFilter=""
                selectedButton?.setBackgroundResource(R.drawable.button_transparent)
                cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, position -> })
                recCardList.adapter = cardListAdapter
                filteredList = cardList.toMutableList()
                provinceTextView.visibility = View.GONE
            }

            filterContainer.addView(btnFilter)
        }
    }

    private fun filterCardList(province: String, breed: String,subBreed:String) {
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
        if(subBreed.isNotEmpty()){
            filteredList = filteredList.filter { it.subBreed == subBreed } as MutableList
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
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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.domain.Breed
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.services.DogDataService
import com.ar.parcialtp3.services.firebase.FirebaseService
import com.ar.parcialtp3.utils.SharedPrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View

    private val firebaseService = FirebaseService()
    private lateinit var publications: List<PublicationEntity>
    private lateinit var searchEditText: AutoCompleteTextView
    private lateinit var suggestionAdapter: ArrayAdapter<String>
    private lateinit var dogServiceAPI: DogDataService
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var filterContainer: LinearLayout

    private lateinit var recCardList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var cardList: MutableList<Card> = ArrayList()
    private lateinit var cardListAdapter: CardAdapter
    lateinit var sharedPreferences: SharedPreferences

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

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeButtonEnabled(true)


        // Find the AutoCompleteTextView by ID
        searchEditText = v.findViewById(R.id.searchEditText)

        // Initialize and set up the suggestion adapter
        suggestionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        searchEditText.setAdapter(suggestionAdapter)


        // Set an item click listener for handling selection
        searchEditText.setOnItemClickListener { parent, view, position, id ->
            val selectedSuggestion = parent.getItemAtPosition(position).toString()
            firebaseService.getPublicationsByBreedOrSubreed(selectedSuggestion){ documents, exception ->
                if (exception == null) {
                    if (documents != null) {
                        publications =
                            documents.mapNotNull { it.toObject(PublicationEntity::class.java) }
                        Log.d("SERVICE", publications.toString())
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


        // Set a text change listener for debouncing and fetching suggestions
        searchEditText.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable?) {
                // Implement your debouncing logic here
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    // This code will be executed after a delay (e.g., 500ms) of no input
                    val input = s.toString()
                    CoroutineScope(Dispatchers.Main).launch {
                        fetchSuggestions(input)
                    }
                }, 500) // Adjust the delay time as needed
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return;
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return;
            }
        })

        return v
    }

    override fun onStart() {
        super.onStart()
        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager
        cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, _->
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
                    Log.d("SERVICE", publications.toString())
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
        Handler().postDelayed({ refreshRecyclerView() }, 2500)
        //TODO Mejorar esto a una coroutina porque me crasheo un par de veces, lo tuve que pasar a 2500ms
    }


    // Function to fetch suggestions based on user input
    private suspend fun fetchSuggestions(input: String) {
        // Check if suggestions for the input are available in the cache
        val cachedSuggestions = getCachedSuggestions(input)
        if (cachedSuggestions != null) {

            suggestionAdapter.clear()
            suggestionAdapter.addAll(cachedSuggestions)
            suggestionAdapter.notifyDataSetChanged()
        } else {

            val breeds:List<Breed> = dogServiceAPI.getAllBreeds()

            val breedSet = mutableSetOf<String>()

            for (breed in breeds) {
                breedSet.add(breed.name)
                breedSet.addAll(breed.subBreeds)
            }

            val breedList = breedSet.toList()


            cacheSuggestions(input, breedList)
            suggestionAdapter.clear()
            suggestionAdapter.addAll(breedList)
            suggestionAdapter.notifyDataSetChanged()


        }
    }

    // Function to retrieve cached suggestions
    private fun getCachedSuggestions(input: String): List<String>? {
        val cachedSuggestions = sharedPreferences.getStringSet(input, null)
        return cachedSuggestions?.toList()
    }



    private fun cacheSuggestions(input: String, suggestions: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet(input, suggestions.toSet())
        editor.apply()
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

            var isClicked = false

            btnFilter.setOnClickListener {
                val filteredList =
                    cardList.filter { it.breed == filterName } as MutableList
                cardListAdapter = CardAdapter(filteredList, this, onClickFavourite = { id, position ->})
                recCardList.adapter = cardListAdapter

                selectedButton?.setBackgroundResource(R.drawable.button_transparent)

                btnFilter.setBackgroundResource(R.drawable.rounded_violet_background_big_radius)

                selectedButton = btnFilter
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
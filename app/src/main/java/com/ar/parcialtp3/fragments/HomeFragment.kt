package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import com.ar.parcialtp3.R
import com.ar.parcialtp3.entities.Breed
import com.ar.parcialtp3.entities.Publish
import com.ar.parcialtp3.services.DogDataService
import com.ar.parcialtp3.services.DogService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var v: View

    var publishList: MutableList<Publish> = ArrayList()
    lateinit var filterContainer: LinearLayout
    private lateinit var searchEditText: AutoCompleteTextView
    private lateinit var suggestionAdapter: ArrayAdapter<String>
    private lateinit var dogServiceAPI: DogDataService

    // Initialize a handler for debouncing user input
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dogServiceAPI= DogDataService()
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

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
            // Handle the selected suggestion (e.g., perform a search with the selected value)
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

    // Function to fetch suggestions based on user input
    private suspend fun fetchSuggestions(input: String) {
        // Check if suggestions for the input are available in the cache
        val cachedSuggestions = getCachedSuggestions(input)
        if (cachedSuggestions != null) {
            // Suggestions found in cache; update the adapter
            suggestionAdapter.clear()
            suggestionAdapter.addAll(cachedSuggestions)
            suggestionAdapter.notifyDataSetChanged()
        } else {
            // Suggestions not found in cache; fetch data from the API

            val breeds:List<Breed> = dogServiceAPI.getAllBreeds()
            val breedList = breeds.map { it.name }
            suggestionAdapter.clear()
            suggestionAdapter.addAll(breedList)
            suggestionAdapter.notifyDataSetChanged()

            // Make an API request based on the user's input
            // Cache the API response for future use
            // Update the suggestionAdapter with the new suggestions
        }
    }

    // Function to retrieve cached suggestions
    private fun getCachedSuggestions(input: String): List<String>? {
        // Implement your caching logic here
        // Return cached suggestions if available, or null if not found
        return null
    }



    fun refreshRecyclerView() {
        val razas = listOf("Golden", "Caniche", "Pastor")
        suggestionAdapter.addAll(razas)
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
                    publishList.filter { it.dog.breed == filter } as MutableList
                //requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                //recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }
}
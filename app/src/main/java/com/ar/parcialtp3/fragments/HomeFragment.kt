package com.ar.parcialtp3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.ar.parcialtp3.R
import com.ar.parcialtp3.entities.Publish

class HomeFragment : Fragment() {

    lateinit var v: View

    var publishList: MutableList<Publish> = ArrayList()
    lateinit var filterContainer: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        return v
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
                    publishList.filter { it.dog.breed == filter } as MutableList
                //requestListAdapter = RequestCardAdapter(filteredList, this@RequestsListFragment)
                //recRequestList.adapter = requestListAdapter
            }

            filterContainer.addView(btnFilter)
        }
    }
}
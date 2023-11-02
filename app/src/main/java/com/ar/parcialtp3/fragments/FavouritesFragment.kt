package com.ar.parcialtp3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.entities.Card
import com.ar.parcialtp3.listener.OnViewItemClickedListener

class FavouritesFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    lateinit var recCardList: RecyclerView
    var cardList: MutableList<Card> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var cardListAdapter: CardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favourites, container, false)
        recCardList = v.findViewById(R.id.rec_card_list)
        return v
    }




    override fun onStart() {
        super.onStart()
        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager
        cardListAdapter = CardAdapter(cardList, this)
        recCardList.adapter = cardListAdapter


        for (i in 0 .. 5) {
            val dog = Card("Falopa", "Salchicha", "Chicha", 20, "Macho", )
            cardList.add(dog)
        }

    }

    override fun onViewItemDetail(card: Card) {
        TODO("Not yet implemented")
    }

}
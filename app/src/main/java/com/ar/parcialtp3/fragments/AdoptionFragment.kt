package com.ar.parcialtp3.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.adapters.CardAdapter
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.services.firebase.FirebaseService

class AdoptionFragment : Fragment(), OnViewItemClickedListener {

    lateinit var v: View
    val firebaseService = FirebaseService()
    lateinit var recCardList: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    var cardList: MutableList<Card> = ArrayList()
    private lateinit var cardListAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_adoption, container, false)
        recCardList = v.findViewById(R.id.adoptionRecyclerView)
        return v
    }

    override fun onStart() {
        super.onStart()

        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager
        //TODO Ver si se puede hacer un constructor diferente o hacer interfaces para evitar tener que pasar más parametros donde no hace falta
        cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, _ ->})
        recCardList.adapter = cardListAdapter

        cardList.clear()

        firebaseService.getPublications(true) { documents, exception ->
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

    override fun onViewItemDetail(card: Card) {
        val action = AdoptionFragmentDirections.actionAdoptionFragmentToDetailFragment(card.id)
        val navController = v.findNavController()
        navController.navigate(action)
    }
}
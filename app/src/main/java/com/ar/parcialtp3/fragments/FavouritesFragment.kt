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
import com.ar.parcialtp3.utils.SharedPrefUtils
import com.google.firebase.ktx.Firebase

class FavouritesFragment : Fragment(), OnViewItemClickedListener {
    lateinit var sharedPrefUtils: SharedPrefUtils
    lateinit var v: View
    lateinit var recCardList: RecyclerView
    var cardList: MutableList<Card> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var cardListAdapter: CardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favourites, container, false)
        recCardList = v.findViewById(R.id.rec_card_list)
        sharedPrefUtils = SharedPrefUtils(requireContext())
        return v
    }


    override fun onStart() {
        super.onStart()
        recCardList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recCardList.layoutManager = linearLayoutManager
        cardListAdapter = CardAdapter(cardList, this, onClickFavourite = { id, position ->
            sharedPrefUtils.toggleFavorite(id)
            val itemOnList = cardList.indexOfFirst { it.id == id }
            cardList.removeAt(position)
            cardListAdapter.notifyDataSetChanged()

        })
        recCardList.adapter = cardListAdapter

        val favList = sharedPrefUtils.getFavouritesFromSharedPrefs()
        for (fav in favList) {
            FirebaseService().getPublicationById(fav) { document, exception ->
                if (exception == null) {
                    if (document != null) {
                        val publication = document.toObject(PublicationEntity::class.java)
                        cardList.add(
                            Card(
                                publication?.dog?.name,
                                publication?.dog?.breed,
                                publication?.dog?.subBreed,
                                publication?.dog?.age,
                                publication?.dog?.sex,
                                document.id
                            )
                        )

                    }

                } else {
                    Log.d("asd", "No hay Favoritos")
                }
                cardListAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onViewItemDetail(card: Card) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailFragment(card.id)
        val navController = v.findNavController()
        navController.navigate(action)
    }

}
package com.ar.parcialtp3.adapters

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.holders.CardHolder
import com.ar.parcialtp3.listener.OnViewItemClickedListener
import com.ar.parcialtp3.utils.SharedPrefUtils
import com.bumptech.glide.Glide

class CardAdapter(
    private val cardList: MutableList<Card>,
    private val onItemClick: OnViewItemClickedListener,
    private val onClickFavourite: (String, Int) -> Unit
) :


    RecyclerView.Adapter<CardHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_recycler, parent, false)
        return (CardHolder(view))
    }

    override fun getItemCount(): Int {
        return cardList.size
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {

        val card = cardList[position]
        holder.setName(card.name)
        holder.setBreed(card.breed)
        holder.setSubBreed(card.subBreed)
        holder.setAge(card.age)
        holder.setSex(card.sex)

        holder.getCardLayout().setOnClickListener {
            onItemClick.onViewItemDetail(card)
        }

        holder.getFavoriteButton().setOnClickListener {
            onClickFavourite.invoke(card.id, position)
            Log.d("id from holder", card.id)
        }


        if (SharedPrefUtils(context).isItemFavourite(card.id)) {
            holder.getFavoriteButton().findViewById<ImageButton>(R.id.favouriteBtn)
                .setImageResource(R.drawable.fav_icon)
        } else {
            holder.getFavoriteButton().findViewById<ImageButton>(R.id.favouriteBtn)
                .setImageResource(R.drawable.favourite_disable)
        }

        if(card.adopted) {
            holder.getFavoriteButton().findViewById<ImageButton>(R.id.favouriteBtn).visibility = View.GONE
        }

        val cardImage = holder.getCardLayout().findViewById<ImageView>(R.id.card_image)
        val imageUrl = card.image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(cardImage)
    }


}
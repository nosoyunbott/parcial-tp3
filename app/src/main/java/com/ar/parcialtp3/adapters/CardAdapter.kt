package com.ar.parcialtp3.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.domain.Card
import com.ar.parcialtp3.holders.CardHolder
import com.ar.parcialtp3.listener.OnViewItemClickedListener

class CardAdapter(private val cardList: MutableList<Card>, private val onItemClick: OnViewItemClickedListener) :

    RecyclerView.Adapter<CardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_recycler, parent, false)
        return (CardHolder(view))
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val card = cardList[position]
        holder.setName(card.name)
        holder.setBreed(card.breed)
        holder.setSubBreed(card.subBreed)
        holder.setAge(card.age)
        holder.setSex(card.sex)

//        holder.setImage(card.image)
        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(card)
        }

    }

}
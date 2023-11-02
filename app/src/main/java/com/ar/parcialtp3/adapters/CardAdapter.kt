package com.ar.parcialtp3.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val requestList: MutableList<Card>,  private val onItemClick: OnViewItemClickedListener) :

    RecyclerView.Adapter<RequestCardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_card_element, parent, false)
        return (RequestCardHolder(view))
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: RequestCardHolder, position: Int) {
        val request = requestList[position]
        holder.setTitle(request.requestTitle)
        holder.setBidsAmount(request.requestBidAmount)
        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(request)
        }

    }

}
package com.ar.parcialtp3.listener

import com.ar.parcialtp3.domain.Card

interface OnViewItemClickedListener {
    fun onViewItemDetail(card: Card)
}
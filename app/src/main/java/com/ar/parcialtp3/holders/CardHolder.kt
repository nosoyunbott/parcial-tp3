package com.ar.parcialtp3.holders


import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R

class CardHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view: View

    init {
        this.view = v
    }

    fun setName(name: String ) {
        val txt : TextView = view.findViewById(R.id.card_name)
        txt.text = name.uppercase()
    }

    fun setBreed(breed: String ) {
        val txt : TextView = view.findViewById(R.id.card_breed)
        txt.text = breed.uppercase()
    }

    fun setSubBreed(subBreed: String ) {
        val txt : TextView = view.findViewById(R.id.card_subBreed)
        txt.text = subBreed.uppercase()
    }

    @SuppressLint("SetTextI18n")
    fun setAge(age: Int ) {
        val txt : TextView = view.findViewById(R.id.card_age)
        txt.text = "$age / ".uppercase()
    }

    fun setSex(sex: String ) {
        val txt : TextView = view.findViewById(R.id.card_sex)
        txt.text = sex.uppercase()
    }

    fun getFavoriteButton(): ImageButton {
        return view.findViewById(R.id.favouriteBtn)
    }

    fun getCardLayout (): CardView {
        return view.findViewById(R.id.card)
    }
}


package com.ar.parcialtp3.holders

import android.view.View
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
        txt.text = name
    }

    fun setBreed(breed: String ) {
        val txt : TextView = view.findViewById(R.id.card_breed)
        txt.text = breed
    }

    fun setSubBreed(subBreed: String ) {
        val txt : TextView = view.findViewById(R.id.card_subBreed)
        txt.text = subBreed
    }

    fun setAge(age: Int ) {
        val txt : TextView = view.findViewById(R.id.card_age)
        txt.text = age.toString()
    }

    fun setSex(sex: String ) {
        val txt : TextView = view.findViewById(R.id.card_sex)
        txt.text = sex
    }

//    fun setImage(image: String ) {
//        val txt : TextView = view.findViewById(R.id.card_image)
//        txt.text = image
//    }

    fun getCardLayout (): CardView {
        return view.findViewById(R.id.card)
    }
}


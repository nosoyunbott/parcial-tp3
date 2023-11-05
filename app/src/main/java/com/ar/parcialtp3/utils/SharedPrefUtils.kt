package com.ar.parcialtp3.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext

class SharedPrefUtils {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listOfFavourites: MutableSet<String>

    fun setFavouritePublication(id: String, context: Context) {
        sharedPreferences = context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        listOfFavourites = sharedPreferences.getStringSet("favourites", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        val editor = sharedPreferences.edit()

        if (listOfFavourites == null) {
            listOfFavourites = mutableSetOf()
        }

        Log.d("sharedPref", listOfFavourites.toString())

        listOfFavourites.add(id)
        editor.putStringSet("favourites", listOfFavourites)
        editor.apply()
    }


    fun resetFavourites(context: Context) {
        sharedPreferences = context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("favourites")
        editor.apply()
    }
}
package com.ar.parcialtp3.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefUtils(val context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
    private lateinit var listOfFavourites: MutableSet<String>
    val editor = sharedPreferences.edit()

    private fun getFavorites(): MutableSet<String> {
        return sharedPreferences.getStringSet("favourites", mutableSetOf()) ?: mutableSetOf()
    }


    /**
     * Agrega favorito/remueve favorito
     */
    fun toggleFavorite(id: String) {
        val favorites = getFavorites()
        val editor = sharedPreferences.edit()

        if (favorites.contains(id)) {
            favorites.remove(id)
        } else {
            favorites.add(id)
        }

        editor.putStringSet("favourites", favorites)
        editor.apply()
    }


    /**
     * Borra la lista de favoritos
     */
    fun resetFavourites() {
        val editor = sharedPreferences.edit()
        editor.remove("favourites")
        editor.apply()
    }


    /**
     * Devuelve la lista de favoritos guardada en sharedPrefs
     */
    fun getFavouritesFromSharedPrefs(): MutableSet<String> {
        var listOfFavourites: MutableSet<String> =
            sharedPreferences.getStringSet("favourites", mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()
        return listOfFavourites
    }


    /**
     * Chequea si un item está en la lista de favoritos.
     */
    fun isItemFavourite(id: String): Boolean {
        listOfFavourites =
            sharedPreferences.getStringSet("favourites", mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()

        return listOfFavourites.contains(id)
    }


    fun saveUserToSharedPref(username: String, phone: String, image: String,) {
        editor.putString("username", username)
        editor.putString("phone", phone)
        editor.putString("image", image)
        editor.apply()
    }
}
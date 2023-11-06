package com.ar.parcialtp3.domain

class Dog(
    val name: String,
    val age: Int,
    val sex: String,
    val breed: String,
    val subBreed: String,
    val images: ArrayList<String>,
    var adopted: Boolean,
    val weight: Int
) {
    constructor() : this("", 0, "", "", "", ArrayList(), false, 0)
}




package com.ar.parcialtp3.domain

class Dog (
    val name: String,
    val age: Int,
    val location: String,
    val sex: String,
    val owner: Owner,
    val breed: String,
    val subBreed: String?,
    val images: MutableList<String>,
    val isAdopted: Boolean
)



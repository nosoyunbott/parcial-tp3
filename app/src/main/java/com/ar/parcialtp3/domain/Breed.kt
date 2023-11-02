package com.ar.parcialtp3.domain

class Breed(name: String?, subBreeds: MutableList<String>) {
    var name: String = ""
    var subBreeds: MutableList<String> = mutableListOf()

    init {
        this.name = name!!
        this.subBreeds = subBreeds!!
    }
}
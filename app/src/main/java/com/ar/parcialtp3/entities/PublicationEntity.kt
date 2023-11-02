package com.ar.parcialtp3.entities

import com.ar.parcialtp3.domain.Dog
import com.ar.parcialtp3.domain.Owner

class PublicationEntity (
    val dog: Dog,
    val owner: Owner,
    val location: String,
    val description: String
)
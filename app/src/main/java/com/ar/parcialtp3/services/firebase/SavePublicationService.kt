package com.ar.parcialtp3.services.firebase

import com.ar.parcialtp3.entities.PublicationEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class SavePublicationService {

    private val db = FirebaseFirestore.getInstance()

    fun savePublication(publication: PublicationEntity): Task<Void> {

        val publicationRef = db.collection("Publications")

        val newDocument = publicationRef.document()

        return newDocument.set(publication)
    }

    //val listImages = arrayListOf("https://images.dog.ceo/breeds/setter-irish/n02100877_123.jpg")
    //val dog = Dog("BoyOlmi",15,"Macho", "Caniche", "Mini", listImages, false)
    //val owner = Owner("Tom", 1156943023, "https://images.dog.ceo/breeds/hound-afghan/n02088094_8063.jpg")
    //val publication = PublicationEntity(dog, owner, Provinces().getList()[5],"Perrito lindo encontrado en la ruta")
    //savePublicationsService.savePublication(publication)
}
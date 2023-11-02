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
}
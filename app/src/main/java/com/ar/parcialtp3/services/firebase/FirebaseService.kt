package com.ar.parcialtp3.services.firebase

import com.ar.parcialtp3.entities.PublicationEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()

     fun getPublications(isAdopted: Boolean, callback: (List<DocumentSnapshot>?, Exception?) -> Unit) {
            db.collection("Publications")
                .whereEqualTo("dog.adopted", isAdopted)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result: QuerySnapshot? = task.result
                        callback(result?.documents, null)
                    } else {
                        callback(null, task.exception)
                    }
                }
        }


    fun getPublicationById(documentId: String, callback: (DocumentSnapshot?, Exception?) -> Unit) {
        db.collection("Publications")
            .document(documentId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot: DocumentSnapshot? = task.result
                    callback(documentSnapshot, null)
                } else {
                    callback(null, task.exception)
                }
            }
    }


    fun savePublication(publication: PublicationEntity): Task<Void> {

        val publicationRef = db.collection("Publications")

        val newDocument = publicationRef.document()

        return newDocument.set(publication)
    }


}
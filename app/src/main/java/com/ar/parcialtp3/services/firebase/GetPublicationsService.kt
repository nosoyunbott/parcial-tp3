package com.ar.parcialtp3.services.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPublicationsService {
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

    // EJEMPLO LLAMADO A LA BASE POR EL SERVICE
    //----------------------------------------------------------------------------------
    /*getPublicationsService.getPublications(false) { documents, exception ->
            if (exception == null) {
                if (documents != null) {
                    for (d in documents) {
                        val publication = d.toObject(PublicationEntity::class.java)
                        if (publication != null) {
                            Log.d("asd", publication.description)
                            Log.d("asd", publication.dog.breed)
                            Log.d("id", d.id)
                        }
                    }
                }
            } else {
                Log.d("asd", "No hay publications")
            }
        }*/

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
}
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

    fun getPublicationsByBreedOrSubreed(breed:String,callback: (List<DocumentSnapshot>?, Exception?) -> Unit)
    {

        val collectionRef = db.collection("Publications")

        val queryByBreed = collectionRef.whereEqualTo("dog.breed", breed)
        val queryBySubBreed = collectionRef.whereEqualTo("dog.subBreed", breed)

        val combinedResults = mutableListOf<DocumentSnapshot>()

        queryByBreed.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: QuerySnapshot? = task.result
                    result?.documents?.let { combinedResults.addAll(it) }
                    queryBySubBreed.get()
                        .addOnCompleteListener { subBreedTask ->
                            if (subBreedTask.isSuccessful) {
                                val subBreedResult: QuerySnapshot? = subBreedTask.result
                                subBreedResult?.documents?.let { combinedResults.addAll(it) }
                                callback(combinedResults, null)
                            } else {
                                callback(null, subBreedTask.exception)
                            }
                        }
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
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

    fun getPublicationsByBreedOrSubreed(breed: String, callback: (List<DocumentSnapshot>?, Exception?) -> Unit) {

        val collectionRef = db.collection("Publications")
        val breedUpperCase = breed.toUpperCase()
        val queryByBreed = collectionRef.whereEqualTo("dog.breed", breedUpperCase)

        queryByBreed.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: QuerySnapshot? = task.result
                    val combinedResults = result?.documents?.toMutableList() ?: mutableListOf()

                    // Check if there are results for the breed
                    if (combinedResults.isNotEmpty()) {
                        callback(combinedResults, null)
                    } else {
                        // No results for the breed, query the subBreed
                        val queryBySubBreed = collectionRef.whereEqualTo("dog.subBreed", breedUpperCase)

                        queryBySubBreed.get()
                            .addOnCompleteListener { subBreedTask ->
                                if (subBreedTask.isSuccessful) {
                                    val subBreedResult: QuerySnapshot? = subBreedTask.result
                                    val subBreedResults = subBreedResult?.documents?.toMutableList() ?: mutableListOf()

                                    callback(subBreedResults, null)
                                } else {
                                    callback(null, subBreedTask.exception)
                                }
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
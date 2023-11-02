package com.ar.parcialtp3.services.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GetPublicationsService {
    private val db = FirebaseFirestore.getInstance()

    //Hay un ejemplo en el onStart del HomeFragment
    fun getPublications(isAdopted: Boolean, callback: (List<DocumentSnapshot>?, Exception?) -> Unit) {
        db.collection("Publications")
            .whereEqualTo("isAdopted", isAdopted)
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
}
package com.ar.parcialtp3.services

import android.util.Log
import com.ar.parcialtp3.entities.Breed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogDataService {
    suspend fun getImagesByBreed(breed: String): List<String> {
        return withContext(Dispatchers.IO) {
            val service = ActivityServiceApiBuilder.create()
            val dataArray = ArrayList<String>()

            try {
                val responseBody = service.getImagesByBreed(breed).execute()
                if (responseBody.isSuccessful) {
                    val jsonResponse = JSONObject(responseBody.body()?.string())
                    val dataObject = jsonResponse.getJSONArray("message")
                    for (i in 0 until dataObject.length()) {
                        val item = dataObject.getString(i)
                        dataArray.add(item)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            dataArray
        }
    }
    suspend fun getAllBreeds(): MutableList<Breed> {
        return withContext(Dispatchers.IO) {
            val service = ActivityServiceApiBuilder.create()
            val dataArray = ArrayList<String>()
            val breeds = mutableListOf<Breed>()
            try {
                val responseBody = service.getAllBreeds().execute()
                if (responseBody.isSuccessful) {
                    val jsonResponse = JSONObject(responseBody.body()?.string())
                    val dataObject = jsonResponse.getJSONObject("message")
                    val breedNames = dataObject.keys()

                    for (breedName in breedNames) {
                        val subBreeds = mutableListOf<String>()
                        val subBreedValue = dataObject.get(breedName)

                        if (subBreedValue is JSONArray) {
                            // If it's an array, iterate through its elements
                            for (i in 0 until subBreedValue.length()) {
                                val subBreed = subBreedValue.getString(i)
                                subBreeds.add(subBreed)
                            }
                        } else if (subBreedValue is String) {
                            // If it's a string, add it directly
                            subBreeds.add(subBreedValue)
                        }

                        val b = Breed(breedName, subBreeds)
                        Log.d("breedName", breedName.toString())
                        Log.d("subBreeds", subBreeds.toString())
                        breeds.add(b)
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            breeds//return
        }

    }

    suspend fun getImagesBySubBreed(breed: String, subBreed: String): List<String> {
        return withContext(Dispatchers.IO) {
            val service = ActivityServiceApiBuilder.create()
            val dataArray = ArrayList<String>()

            try {
                val responseBody = service.getImagesBySubBreed(breed, subBreed).execute()
                if (responseBody.isSuccessful) {
                    val jsonResponse = JSONObject(responseBody.body()?.string())
                    val dataObject = jsonResponse.getJSONArray("message")
                    for (i in 0 until dataObject.length()) {
                        val item = dataObject.getString(i)
                        dataArray.add(item)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            dataArray
        }
    }
}
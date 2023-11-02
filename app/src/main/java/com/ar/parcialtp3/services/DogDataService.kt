package com.ar.parcialtp3.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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
    suspend fun getAllBreeds() {
        return withContext(Dispatchers.IO) {
            val service = ActivityServiceApiBuilder.create()
            val dataArray = ArrayList<String>()

            try {
                val responseBody = service.getAllBreeds().execute()
                if (responseBody.isSuccessful) {
                    val jsonResponse = JSONObject(responseBody.body()?.string())
                    val dataObject = jsonResponse.getJSONObject("message")
                    Log.d("asddsasdadsadsa", dataObject.toString())
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            dataArray
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
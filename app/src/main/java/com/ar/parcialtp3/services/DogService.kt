package com.ar.parcialtp3.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogService {
    @GET("breeds/list/all")
    fun getAllBreeds(): Call<ResponseBody>

    @GET("breed/{breed}/images")
    fun getImagesByBreed(@Path(value="breed") breed: String): Call<ResponseBody>

    @GET("breed/{breed}/{sub-breed}/images")
    fun getImagesBySubBreed(@Path(value="breed", encoded = true) breed: String): Call<ResponseBody>

}
package com.alperen.moviebox.network.retrofitapi

import com.alperen.moviebox.models.user.show.ModelShow
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("/shows")
    fun getShows(): Call<ArrayList<ModelShow>>
}
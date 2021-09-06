package com.alperen.moviebox.network.retrofitapi

import com.alperen.moviebox.models.show.ModelSearch
import com.alperen.moviebox.models.user.show.ModelShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/shows")
    fun getShows(): Call<ArrayList<ModelShow>>

    @GET("/search/shows")
    fun searchShows(@Query("q") text: String): Call<ArrayList<ModelSearch>>
}
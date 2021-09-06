package com.alperen.moviebox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.models.show.ModelSearch
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.network.retrofitapi.APIService
import com.alperen.moviebox.network.retrofitapi.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class HomePageViewModel(state: SavedStateHandle) : BaseViewModel(state) {
    var showList = MutableLiveData<ArrayList<ModelShow>>()
    var errorMsg = MutableLiveData<Any>()


    fun searchShows(text: String) {
        val searchList = MutableLiveData<ArrayList<ModelSearch>>()
        val service = RetrofitInstance.getInstance().create(APIService::class.java)
        val request = service.searchShows(text)

        request.enqueue(object : Callback<ArrayList<ModelSearch>> {
            override fun onResponse(
                call: Call<ArrayList<ModelSearch>>,
                response: Response<ArrayList<ModelSearch>>
            ) {
                showList.value?.clear()
                if (response.isSuccessful) {
                    searchList.value = response.body()
                    searchList.observeForever { search ->
                        search.forEach {
                            showList.value?.add(it.show!!)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelSearch>>, t: Throwable) {
                errorMsg.value = t.message.toString()
                Log.e("retrofit", t.message.toString())
            }

        })
    }

    fun getShows() {
        val service = RetrofitInstance.getInstance().create(APIService::class.java)
        val request = service.getShows()

        request.enqueue(object : Callback<ArrayList<ModelShow>> {
            override fun onResponse(
                call: Call<ArrayList<ModelShow>>,
                response: Response<ArrayList<ModelShow>>
            ) {
                if (response.isSuccessful) {
                    showList.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelShow>>, t: Throwable) {
                errorMsg.value = t.message.toString()
            }
        })
    }

    fun getShowsAsc() {
        val service = RetrofitInstance.getInstance().create(APIService::class.java)
        val request = service.getShows()

        request.enqueue(object : Callback<ArrayList<ModelShow>> {
            override fun onResponse(
                call: Call<ArrayList<ModelShow>>,
                response: Response<ArrayList<ModelShow>>
            ) {
                if (response.isSuccessful) {
                    // TODO: Hatalı
                    response.body()?.sortByDescending { it.rating?.average }
                    showList.value = response.body()
                    showList.value?.reversed()
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelShow>>, t: Throwable) {
                errorMsg.value = t.message.toString()
            }
        })
    }

    fun getShowsDesc() {
        val service = RetrofitInstance.getInstance().create(APIService::class.java)
        val request = service.getShows()

        request.enqueue(object : Callback<ArrayList<ModelShow>> {
            override fun onResponse(
                call: Call<ArrayList<ModelShow>>,
                response: Response<ArrayList<ModelShow>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.sortByDescending { it.rating?.average }
                    showList.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelShow>>, t: Throwable) {
                errorMsg.value = t.message.toString()
            }
        })
    }
}
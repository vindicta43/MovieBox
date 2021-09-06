package com.alperen.moviebox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alperen.moviebox.models.show.ModelSearch
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.network.FirebaseUserUtils
import com.alperen.moviebox.network.retrofitapi.APIService
import com.alperen.moviebox.network.retrofitapi.RetrofitInstance
import com.alperen.moviebox.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageViewModel(state: SavedStateHandle) : BaseViewModel(state) {
    var showList = MutableLiveData<ArrayList<ModelShow>>()
    var errorMsg = MutableLiveData<Any>()

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
}
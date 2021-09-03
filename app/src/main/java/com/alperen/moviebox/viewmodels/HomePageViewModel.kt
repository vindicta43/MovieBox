package com.alperen.moviebox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
                    Log.e("retrofit", response.body().toString())
                    showList.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelShow>>, t: Throwable) {
                errorMsg.value = t.message.toString()
            }
        })
    }
}
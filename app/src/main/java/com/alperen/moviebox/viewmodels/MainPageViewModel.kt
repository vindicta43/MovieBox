package com.alperen.moviebox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.network.retrofitapi.APIService
import com.alperen.moviebox.network.retrofitapi.RetrofitInstance
import com.alperen.moviebox.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPageViewModel(state: SavedStateHandle) : BaseViewModel(state) {
//    var showList = MutableLiveData<ArrayList<ModelShow>>()

    var showList = arrayListOf<ModelShow>()
    fun getShows(): MutableLiveData<Map<String, Any>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Any()))
        val service = RetrofitInstance.getInstance().create(APIService::class.java)
        val request = service.getShows()

        request.enqueue(object : Callback<ArrayList<ModelShow>> {
            override fun onResponse(
                call: Call<ArrayList<ModelShow>>,
                response: Response<ArrayList<ModelShow>>
            ) {
                if (response.isSuccessful) {
                    result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
                    Log.e("retrofit", response.body().toString())
                    showList = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<ModelShow>>, t: Throwable) {
                result.value = mapOf(Constants.FAILED to t.message.toString())
            }
        })

        return result
    }
}
package com.alperen.moviebox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alperen.moviebox.models.user.ModelUser
import com.alperen.moviebox.network.FirebaseUserUtils
import kotlin.random.Random

open class MainViewModel(private val state: SavedStateHandle): ViewModel() {
    private val liveDate = state.getLiveData("liveData", Random.nextInt().toString())

    fun saveState() {
        state.set("liveData", liveDate.value)
    }

    fun getUserDetails(): MutableLiveData<ModelUser> {
        val result = MutableLiveData<ModelUser>()
        FirebaseUserUtils.getUserDetails().observeForever { observer ->
            result.value = observer
        }
        return result
    }
}
package com.alperen.moviebox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.network.FirebaseUserUtils

class RegisterViewModel(state: SavedStateHandle) : BaseViewModel(state) {
    fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData<Map<String, String>>()
        FirebaseUserUtils.register(name, surname, email, password)
            .observeForever { observer ->
                result.value = observer
            }
        return result
    }
}
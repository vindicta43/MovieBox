package com.alperen.moviebox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.network.FirebaseUserUtils

class LoginViewModel(state: SavedStateHandle) : BaseViewModel(state) {
    fun sendResetMail(email: String): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData<Map<String, String>>()
        FirebaseUserUtils.sendResetEmail(email).observeForever { observer ->
            result.value = observer
        }
        return result
    }

    fun login(email: String, password: String): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData<Map<String, String>>()
        FirebaseUserUtils.login(email, password).observeForever { observer ->
            result.value = observer
        }
        return result
    }
}
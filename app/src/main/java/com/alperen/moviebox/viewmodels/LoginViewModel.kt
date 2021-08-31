package com.alperen.moviebox.viewmodels

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.R
import com.alperen.moviebox.network.FirebaseUserUtils
import com.alperen.moviebox.utils.AlertBuilder

class LoginViewModel(state: SavedStateHandle) : MainViewModel(state) {
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
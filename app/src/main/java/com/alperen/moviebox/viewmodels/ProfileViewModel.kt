package com.alperen.moviebox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.network.FirebaseUserUtils

class ProfileViewModel(state: SavedStateHandle): MainViewModel(state) {
    fun updateUser(
        name: String,
        surname: String,
        email: String,
        pass: String,
        newPass: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData<Map<String, String>>()
        FirebaseUserUtils.updateUser(name, surname, email, pass, newPass).observeForever { observer ->
            result.value = observer
        }
        return result
    }

    fun updateUserWithNoPassword(
        name: String,
        surname: String,
        email: String,
        pass: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData<Map<String, String>>()
        FirebaseUserUtils.updateUserWithNoPassword(name, surname, email, pass).observeForever { observer ->
            result.value = observer
        }
        return result
    }
}
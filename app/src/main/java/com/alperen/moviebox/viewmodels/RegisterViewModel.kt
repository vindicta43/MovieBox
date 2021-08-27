package com.alperen.moviebox.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.alperen.moviebox.network.FirebaseUserUtils

class RegisterViewModel(state: SavedStateHandle): MainViewModel(state) {
    fun register(context: Context?, nav: NavController, email: String, password: String){
        FirebaseUserUtils.register(context, nav,email, password)
    }
}
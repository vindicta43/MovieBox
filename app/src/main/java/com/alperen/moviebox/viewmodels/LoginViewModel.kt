package com.alperen.moviebox.viewmodels

import android.content.Context
import android.text.Editable
import androidx.lifecycle.SavedStateHandle
import com.alperen.moviebox.R
import com.alperen.moviebox.network.FirebaseUserUtils
import com.alperen.moviebox.utils.AlertBuilder

class LoginViewModel(state: SavedStateHandle): MainViewModel(state) {
    fun sendResetMail(context: Context?, email: Editable?) {
        if (email.isNullOrBlank()) {
            val title = context?.resources?.getString(R.string.alert_dialog_warn)
            val msg = context?.resources?.getString(R.string.alert_dialog_empty_email)

            AlertBuilder(context).build(title, msg)
        } else {
            FirebaseUserUtils.sendResetEmail(context, email)
        }
    }
}
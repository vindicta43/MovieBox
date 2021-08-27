package com.alperen.moviebox.network

import android.content.Context
import android.text.Editable
import androidx.navigation.NavController
import com.alperen.moviebox.R
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.ToastBuilder
import com.google.firebase.auth.FirebaseAuth

object FirebaseUserUtils {
    fun sendResetEmail(context: Context?, email: Editable?) {
        val mail = email.toString()
        val title = context?.resources?.getString(R.string.alert_dialog_success)
        val err = context?.resources?.getString(R.string.alert_dialog_error)
        val msg = context?.resources?.getString(R.string.alert_dialog_reset_mail_success)

        FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnSuccessListener {
            AlertBuilder(context).build(title, msg)
        }.addOnFailureListener {
            AlertBuilder(context).build(err, it.message.toString())
        }
    }

    fun register(context: Context?, nav: NavController, email: String, password: String) {
        val msg = context?.resources?.getString(R.string.alert_dialog_success)
        val err = context?.resources?.getString(R.string.alert_dialog_error)

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                ToastBuilder(context).build(msg)
                nav.navigate(R.id.action_registerFragment_to_loginFragment)
            }.addOnFailureListener {
                AlertBuilder(context).build(err, it.message.toString())
            }
    }
}
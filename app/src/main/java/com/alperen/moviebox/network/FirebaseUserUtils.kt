package com.alperen.moviebox.network

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.alperen.moviebox.R
import com.alperen.moviebox.models.ModelUser
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.ToastBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

    fun register(
        context: Context?,
        name: String,
        surname: String,
        email: String,
        password: String
    ): MutableLiveData<String> {
        val msg = context?.resources?.getString(R.string.alert_dialog_success)
        val err = context?.resources?.getString(R.string.alert_dialog_error)
        val result = MutableLiveData("Processing")

        val auth = FirebaseAuth.getInstance()
        val dbRef = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = ModelUser(auth.uid.toString(), name, surname, email)
                dbRef
                    .collection("users")
                    .document(auth.uid.toString())
                    .set(user)
                    .addOnSuccessListener {
                        ToastBuilder(context).build(msg)
                        result.value = "Success"
                    }.addOnFailureListener {
                        AlertBuilder(context).build(err, it.message.toString())
                        result.value = "Fail"
                    }
            }.addOnFailureListener {
                AlertBuilder(context).build(err, it.message.toString())
                result.value = "Fail"
            }
        return result
    }

    fun login(context: Context?, email: String, password: String): MutableLiveData<String> {
        val msg = context?.resources?.getString(R.string.alert_dialog_success)
        val err = context?.resources?.getString(R.string.alert_dialog_error)
        val result = MutableLiveData("Processing")

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                ToastBuilder(context).build(msg)
                result.value = "Success"
            }.addOnFailureListener {
                AlertBuilder(context).build(err, it.message.toString())
                result.value = "Fail"
            }
        return result
    }
}
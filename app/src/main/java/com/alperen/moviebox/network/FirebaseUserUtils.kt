package com.alperen.moviebox.network

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.alperen.moviebox.R
import com.alperen.moviebox.models.user.ModelUser
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.ToastBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUserUtils {
    fun sendResetEmail(email: String): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Constants.PROCESSING))

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener {
            result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
        }.addOnFailureListener {
            result.value = mapOf(Constants.FAILED to it.message.toString())
        }
        return result
    }

    fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Constants.PROCESSING))

        val auth = FirebaseAuth.getInstance()
        val dbRef = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = ModelUser(auth.uid.toString(), name, surname, email)
                dbRef
                    .collection(Constants.COLLECTION_USERS)
                    .document(auth.uid.toString())
                    .set(user)
                    .addOnSuccessListener {
                        result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
                    }.addOnFailureListener {
                        result.value = mapOf(Constants.FAILED to it.message.toString())
                    }
            }.addOnFailureListener {
                result.value = mapOf(Constants.FAILED to it.message.toString())
            }
        return result
    }

    fun login(email: String, password: String): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Constants.PROCESSING))
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
            }.addOnFailureListener {
                result.value = mapOf(Constants.FAILED to it.message.toString())
            }
        return result
    }
}
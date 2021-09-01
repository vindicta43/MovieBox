package com.alperen.moviebox.network

import androidx.lifecycle.MutableLiveData
import com.alperen.moviebox.models.user.ModelUser
import com.alperen.moviebox.utils.Constants
import com.google.firebase.auth.EmailAuthProvider
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

    fun getUserDetails(): MutableLiveData<ModelUser> {
        val result = MutableLiveData<ModelUser>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseFirestore.getInstance()

        dbRef.collection(Constants.COLLECTION_USERS)
            .document(userId!!)
            .get()
            .addOnSuccessListener {
                val user = ModelUser(
                    it[Constants.ID].toString(),
                    it[Constants.NAME].toString(),
                    it[Constants.SURNAME].toString(),
                    it[Constants.EMAIL].toString(),
                )
                result.value = user
            }
        return result
    }

    fun updateUser(
        name: String,
        surname: String,
        email: String,
        pass: String,
        newPass: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Constants.PROCESSING))
        val user = FirebaseAuth.getInstance().currentUser
        val dbRef = FirebaseFirestore.getInstance()
        val credentials = EmailAuthProvider.getCredential(user?.email!!, pass)

        user.reauthenticate(credentials)
            .addOnSuccessListener {
                user.updateEmail(email)
                    .addOnSuccessListener {
                        user.updatePassword(newPass)
                            .addOnSuccessListener {
                                dbRef.collection(Constants.COLLECTION_USERS)
                                    .document(user.uid)
                                    .set(
                                        ModelUser(
                                            user.uid,
                                            name,
                                            surname,
                                            email
                                        )
                                    )
                                    .addOnSuccessListener {
                                        result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
                                    }.addOnFailureListener {
                                        result.value =
                                            mapOf(Constants.FAILED to it.message.toString())
                                    }
                            }.addOnFailureListener {
                                result.value = mapOf(Constants.FAILED to it.message.toString())
                            }
                    }.addOnFailureListener {
                        result.value = mapOf(Constants.FAILED to it.message.toString())
                    }
            }.addOnFailureListener {
                result.value = mapOf(Constants.FAILED to it.message.toString())
            }
        return result
    }

    fun updateUserWithNoPassword(
        name: String,
        surname: String,
        email: String,
        pass: String
    ): MutableLiveData<Map<String, String>> {
        val result = MutableLiveData(mapOf(Constants.PROCESSING to Constants.PROCESSING))
        val user = FirebaseAuth.getInstance().currentUser
        val dbRef = FirebaseFirestore.getInstance()
        val credentials = EmailAuthProvider.getCredential(user?.email!!, pass)

        user.reauthenticate(credentials)
            .addOnSuccessListener {
                user.updateEmail(email)
                    .addOnSuccessListener {
                        dbRef.collection(Constants.COLLECTION_USERS)
                            .document(user.uid)
                            .set(
                                ModelUser(
                                    user.uid,
                                    name,
                                    surname,
                                    email
                                )
                            )
                            .addOnSuccessListener {
                                result.value = mapOf(Constants.SUCCESS to Constants.SUCCESS)
                            }.addOnFailureListener {
                                result.value = mapOf(Constants.FAILED to it.message.toString())
                            }
                    }.addOnFailureListener {
                        result.value = mapOf(Constants.FAILED to it.message.toString())
                    }
            }.addOnFailureListener {
                result.value = mapOf(Constants.FAILED to it.message.toString())
            }
        return result
    }
}
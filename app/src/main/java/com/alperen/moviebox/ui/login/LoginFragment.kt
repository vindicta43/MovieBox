package com.alperen.moviebox.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentLoginBinding
import com.alperen.moviebox.utils.AlertBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        with(binding) {
            btnLogin.setOnClickListener {

            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPassword.setOnClickListener {
                sendResetMail(etEmail)
            }

            return root
        }
    }

    private fun sendResetMail(etEmail: TextInputEditText) {
        if (etEmail.text.isNullOrEmpty()) {
            val title = resources.getString(R.string.alert_dialog_warn)
            val msg = resources.getString(R.string.alert_dialog_empty_email)

            AlertBuilder(context).build(title, msg)
        } else {
            val email = etEmail.text.toString()
            val title = resources.getString(R.string.alert_dialog_success)
            val err = resources.getString(R.string.alert_dialog_error)
            val msg = resources.getString(R.string.alert_dialog_reset_mail_success)

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener {
                AlertBuilder(context).build(title, msg)
            }.addOnFailureListener {
                AlertBuilder(context).build(err, it.message.toString())
            }
        }
    }
}
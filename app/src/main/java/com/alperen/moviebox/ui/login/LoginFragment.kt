package com.alperen.moviebox.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentLoginBinding
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                LoginViewModel::class.java
            )

        with(binding) {
            btnLogin.setOnClickListener {
                val err = resources.getString(R.string.alert_dialog_error)
                val msg = resources.getString(R.string.alert_dialog_empty_space)
                val email = etEmail.text
                val password = etPassword.text
                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    AlertBuilder(context).build(err, msg)
                } else {
                    viewModel.login(context, email.toString(), password.toString()).observe(viewLifecycleOwner, { result ->
                        when(result) {
                            "Processing" -> {
                                loadingDialog.show(activity?.supportFragmentManager!!, "loader")
                            }
                            "Success" -> {
                                loadingDialog.dismissAllowingStateLoss()
                                findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                            }
                            "Fail" -> {
                                loadingDialog.dismissAllowingStateLoss()
                            }
                        }
                    })
                }
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPassword.setOnClickListener {
                viewModel.sendResetMail(context, etEmail.text)
            }
            return root
        }
    }
}
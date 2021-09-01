package com.alperen.moviebox.ui.login.login

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
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.utils.ToastBuilder
import com.alperen.moviebox.viewmodels.LoginViewModel

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
            val email = etEmail.text
            val password = etPassword.text

            btnLogin.setOnClickListener {
                val err = resources.getString(R.string.alert_dialog_error)
                val msg = resources.getString(R.string.alert_dialog_empty_space)

                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    AlertBuilder(context).build(err, msg)
                } else {
                    viewModel.login(email.toString(), password.toString())
                        .observe(viewLifecycleOwner, { result ->
                            observeLoginResult(result)
                        })
                }
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgotPassword.setOnClickListener {
                if (email.isNullOrBlank()) {
                    val title = resources.getString(R.string.alert_dialog_error)
                    val msg = resources.getString(R.string.alert_dialog_empty_email)
                    AlertBuilder(context).build(title, msg)
                } else {
                    viewModel.sendResetMail(email.toString())
                        .observe(viewLifecycleOwner, { result ->
                            observeResetMailResult(result)
                        })
                }
            }
            return root
        }
    }

    private fun observeResetMailResult(result: Map<String, String>) {
        val title = resources.getString(R.string.alert_dialog_success)
        val titleFail = resources.getString(R.string.alert_dialog_error)
        val msg = resources.getString(R.string.alert_dialog_reset_mail_success)

        if (result.containsKey(Constants.PROCESSING)) {
            loadingDialog.show(
                activity?.supportFragmentManager!!,
                "loader"
            )
        }
        if (result.containsKey(Constants.SUCCESS)) {
            loadingDialog.dismissAllowingStateLoss()
            AlertBuilder(context).build(title, msg)
        }
        if (result.containsKey(Constants.FAILED)) {
            loadingDialog.dismissAllowingStateLoss()
            AlertBuilder(context).build(titleFail, result[Constants.FAILED])
        }
    }

    private fun observeLoginResult(result: Map<String, String>) {
        val msg = resources.getString(R.string.alert_dialog_success)
        val title = resources.getString(R.string.alert_dialog_error)

        if (result.containsKey(Constants.PROCESSING)) {
            loadingDialog.show(
                activity?.supportFragmentManager!!,
                "loader"
            )
        }
        if (result.containsKey(Constants.SUCCESS)) {
            loadingDialog.dismissAllowingStateLoss()
            ToastBuilder(context).build(msg)
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            activity?.finish()
        }
        if (result.containsKey(Constants.FAILED)) {
            loadingDialog.dismissAllowingStateLoss()
            AlertBuilder(context).build(title, result[Constants.FAILED])
        }
    }
}
package com.alperen.moviebox.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentRegisterBinding
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.utils.ToastBuilder
import com.alperen.moviebox.viewmodels.RegisterViewModel

open class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                RegisterViewModel::class.java
            )

        with(binding) {
            btnRegister.setOnClickListener {
                val name = etName.text
                val surname = etSurname.text
                val email = etEmail.text
                val password = etPassword.text
                val passwordApply = etPasswordApply.text

                val err = resources.getString(R.string.alert_dialog_error)
                val msgEmpty = resources.getString(R.string.alert_dialog_empty_space)
                val msgNotEqual = resources.getString(R.string.alert_dialog_password_doesnt_match)

                if (name.isNullOrEmpty() &&
                    surname.isNullOrEmpty() &&
                    email.isNullOrEmpty() &&
                    password.isNullOrEmpty() &&
                    passwordApply.isNullOrEmpty()
                ) {
                    AlertBuilder(context).build(err, msgEmpty)
                } else {
                    if (password?.equals(passwordApply)!!) {
                        AlertBuilder(context).build(err, msgNotEqual)
                    } else {
                        viewModel.register(
                            name.toString(),
                            surname.toString(),
                            email.toString(),
                            password.toString()
                        )
                            .observe(viewLifecycleOwner, { result ->
                                observeResult(result)
                            })
                    }
                }
            }

            return root
        }
    }

    private fun observeResult(result: Map<String, String>) {
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
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }
        if (result.containsKey(Constants.FAILED)) {
            loadingDialog.dismissAllowingStateLoss()
            AlertBuilder(context).build(title, result[Constants.FAILED])
        }
    }
}
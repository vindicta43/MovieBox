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
import com.alperen.moviebox.viewmodels.RegisterViewModel

open class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
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
                val name = etName.text.toString()
                val surname = etSurname.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val passwordApply = etPasswordApply.text.toString()

                val err = resources.getString(R.string.alert_dialog_error)
                val msgEmpty = resources.getString(R.string.alert_dialog_empty_space)
                val msgNotEqual = resources.getString(R.string.alert_dialog_password_doesnt_match)

                if (name.isBlank() &&
                    surname.isBlank() &&
                    email.isBlank() &&
                    password.isBlank() &&
                    passwordApply.isBlank()
                ) {
                    AlertBuilder(context).build(err, msgEmpty)
                } else {
                    if (password != passwordApply) {
                        AlertBuilder(context).build(err, msgNotEqual)
                    } else {
                        viewModel.register(context, findNavController(), email, password)
                    }
                }
            }

            return root
        }
    }

}
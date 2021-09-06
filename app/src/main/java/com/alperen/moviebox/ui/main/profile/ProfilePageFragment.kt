package com.alperen.moviebox.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentProfilePageBinding
import com.alperen.moviebox.ui.login.LoginActivity
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.viewmodels.ProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ProfilePageFragment : Fragment() {
    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var viewModel: ProfileViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_page, container, false)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                ProfileViewModel::class.java
            )

        with(binding) {
            initFields(
                etName,
                etSurname,
                etEmail
            )

            btnUpdate.setOnClickListener {
                val title = resources.getString(R.string.alert_dialog_error)
                val msg = resources.getString(R.string.alert_dialog_update_empty_space)
                val name = etName.text
                val surname = etSurname.text
                val email = etEmail.text
                val pass = etAccountPassword.text
                val newPass = etNewPassword.text

                if (fieldsAreEmpty(name, surname, email, pass)) {
                    AlertBuilder(context).build(title, msg)
                } else {
                    if (newPass.isNullOrEmpty()) {
                        viewModel.updateUserWithNoPassword(
                            name.toString(),
                            surname.toString(),
                            email.toString(),
                            pass.toString()
                        ).observe(viewLifecycleOwner, { result ->
                            observeUpdateResult(result)
                        })
                    } else {
                        viewModel.updateUser(
                            name.toString(),
                            surname.toString(),
                            email.toString(),
                            pass.toString(),
                            newPass.toString()
                        ).observe(viewLifecycleOwner, { result ->
                            observeUpdateResult(result)
                            // clear fields
                            etAccountPassword.setText("")
                            etNewPassword.setText("")
                        })
                    }
                }
            }

            btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                setStartToLogin()
            }
            return root
        }
    }

    private fun observeUpdateResult(result: Map<String, String>) {
        val title = resources.getString(R.string.alert_dialog_success)
        val titleFail = resources.getString(R.string.alert_dialog_error)
        val msg = resources.getString(R.string.alert_dialog_update_profile_success)

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

    private fun fieldsAreEmpty(
        name: Editable?,
        surname: Editable?,
        email: Editable?,
        password: Editable?
    ): Boolean {
        return name.isNullOrEmpty() || surname.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()
    }

    private fun initFields(
        name: TextInputEditText,
        surname: TextInputEditText,
        email: TextInputEditText
    ) {
        viewModel.getUserDetails().observe(viewLifecycleOwner, { result ->
            name.setText(result.name)
            surname.setText(result.surname)
            email.setText(result.email)
        })
    }

    private fun setStartToLogin() {
        makeAppStarted()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }

    private fun makeAppStarted() {
        val sharedPref = activity?.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("isAppStarted", true)
        editor?.apply()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::viewModel.isInitialized)
            viewModel.saveState()

        super.onSaveInstanceState(outState)
    }
}
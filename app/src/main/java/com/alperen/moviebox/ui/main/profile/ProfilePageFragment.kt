package com.alperen.moviebox.ui.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentProfilePageBinding
import com.alperen.moviebox.ui.login.LoginActivity
import com.alperen.moviebox.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfilePageFragment : Fragment() {
    private lateinit var binding: FragmentProfilePageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_page, container, false)

        with(binding) {
            btnLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                setStartToLogin()
            }

            return root
        }
    }

    fun setStartToLogin() {
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
}
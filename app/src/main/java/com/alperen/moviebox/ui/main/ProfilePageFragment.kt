package com.alperen.moviebox.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentProfilePageBinding
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
                findNavController().navigate(R.id.action_profilePageFragment_to_loginFragment)
            }

            return root
        }
    }
}
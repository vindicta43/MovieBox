package com.alperen.moviebox.ui.onboard.pages

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.animation.content.Content
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentPage3Binding

class Page3Fragment : Fragment() {
    private lateinit var binding: FragmentPage3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_page3, container, false)

        with(binding) {
            tvSkip.setOnClickListener {
                onboardingShowed()
                findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            }
            return root
        }
    }

    private fun onboardingShowed() {
        val sharedPref = activity?.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("Finished", true)
        editor?.apply()
    }
}
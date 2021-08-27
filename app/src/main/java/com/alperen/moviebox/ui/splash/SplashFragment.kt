package com.alperen.moviebox.ui.splash

import android.animation.Animator
import android.content.Context
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
import com.alperen.moviebox.databinding.FragmentSplashBinding
import com.alperen.moviebox.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                MainViewModel::class.java
            )

        with(binding) {
            splash.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (isAlreadyLoggedIn()) {
                        findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                    } else {
                        initApp()
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })

            return root
        }
    }

    fun isAlreadyLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }

    private fun initApp() {
        if (onboardingShowed()) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
        }
    }

    private fun onboardingShowed(): Boolean {
        val sharedPref = activity?.getSharedPreferences("", Context.MODE_PRIVATE)
        return sharedPref?.getBoolean("Finished", false)!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::viewModel.isInitialized)
            viewModel.saveState()

        super.onSaveInstanceState(outState)
    }
}
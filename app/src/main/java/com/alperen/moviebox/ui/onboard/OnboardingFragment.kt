package com.alperen.moviebox.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentOnboardingBinding
import com.alperen.moviebox.ui.onboard.pages.Page1Fragment
import com.alperen.moviebox.ui.onboard.pages.Page2Fragment
import com.alperen.moviebox.ui.onboard.pages.Page3Fragment
import com.alperen.moviebox.utils.OnboardingViewPagerAdapter

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)

        with(binding) {
            inflatePager(pager)

            return root
        }
    }

    private fun inflatePager(pager: ViewPager2) {
        val pages = arrayListOf(
            Page1Fragment(),
            Page2Fragment(),
            Page3Fragment()
        )

        val adapter =
            OnboardingViewPagerAdapter(pages, requireActivity().supportFragmentManager, lifecycle)

        pager.adapter = adapter
    }
}
package com.alperen.moviebox.ui.main.details

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alperen.moviebox.databinding.FragmentDetailsBinding
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.viewmodels.BaseViewModel
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: BaseViewModel
    val args: DetailsFragmentArgs by navArgs()
    lateinit var show: ModelShow
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                BaseViewModel::class.java
            )

        with(binding) {
            val progress = CircularProgressDrawable(requireContext())
            progress.centerRadius = 16f
            progress.start()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.text = Html.fromHtml(args.show.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                tvContent.text = args.show.summary
            }
            toolbar.title = args.show.name
            Glide.with(requireContext()).load(args.show.image).placeholder(progress).into(ivImageBig)

            return root
        }
    }
}
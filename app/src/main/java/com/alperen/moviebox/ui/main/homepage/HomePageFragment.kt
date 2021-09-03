package com.alperen.moviebox.ui.main.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentHomePageBinding
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.viewmodels.MainPageViewModel

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: MainPageViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                MainPageViewModel::class.java
            )

        with(binding) {
            viewModel.getShows()
            loadingDialog.show(activity?.supportFragmentManager!!, "loader")

            viewModel.showList.observe(viewLifecycleOwner, { modelShowList ->
                loadingDialog.dismissAllowingStateLoss()
                mainRecycler.adapter = HomePageRecyclerAdapter(modelShowList)
                mainRecycler.layoutManager = LinearLayoutManager(context)

                val genresSet = mutableSetOf<String?>()
                modelShowList.forEach { shows ->
                    shows.genres?.forEach {
                        genresSet.add(it)
                    }
                }
                val genresList = arrayListOf<String?>()
                genresSet.forEach {
                    genresList.add(it)
                }

                spnGenres.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    genresList
                )
            })

            viewModel.errorMsg.observe(viewLifecycleOwner, {
                val title = resources.getString(R.string.alert_dialog_error)
                AlertBuilder(context).build(title, it.toString())
                loadingDialog.dismissAllowingStateLoss()
            })

            return root
        }
    }
}

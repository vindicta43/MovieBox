package com.alperen.moviebox.ui.main.homepage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentHomePageBinding
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.viewmodels.HomePageViewModel
import java.util.*
import kotlin.collections.ArrayList

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var viewModel: HomePageViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                HomePageViewModel::class.java
            )
        var showsList = arrayListOf<ModelShow>()

        with(binding) {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        spnGenres.setSelection(0)
                    } else {
                        viewModel.searchShows(p0.toString())
                        viewModel.showList.observe(viewLifecycleOwner, {
                            viewModel.getUserDetails().observeForever {
                                mainRecycler.adapter = HomePageRecyclerAdapter(showsList)
                                mainRecycler.layoutManager = LinearLayoutManager(context)
                            }
                        })
                    }
                }
            })

            spnGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> {
                            loadingDialog.show(activity?.supportFragmentManager!!, "loader")
                            viewModel.getShows()
                            viewModel.showList.observe(viewLifecycleOwner, { observeShowsList ->
                                showsList = observeShowsList
                            })

                            viewModel.getUserDetails().observe(viewLifecycleOwner, {
                                mainRecycler.adapter = HomePageRecyclerAdapter(showsList)
                                mainRecycler.layoutManager = LinearLayoutManager(context)
                                loadingDialog.dismissAllowingStateLoss()
                                notifyChange()
                            })
                        }
                        1 -> {
                            loadingDialog.show(activity?.supportFragmentManager!!, "loader")
                            viewModel.getShowsAsc()
                            viewModel.showList.observe(viewLifecycleOwner, { observeShowsList ->
                                showsList = observeShowsList
                            })

                            viewModel.getUserDetails().observe(viewLifecycleOwner, {
                                mainRecycler.adapter = HomePageRecyclerAdapter(showsList)
                                mainRecycler.layoutManager = LinearLayoutManager(context)
                                loadingDialog.dismissAllowingStateLoss()
                                notifyChange()
                            })
                        }
                        2 -> {
                            loadingDialog.show(activity?.supportFragmentManager!!, "loader")
                            viewModel.getShowsDesc()
                            viewModel.showList.observe(viewLifecycleOwner, { observeShowsList ->
                                showsList = observeShowsList
                            })

                            viewModel.getUserDetails().observe(viewLifecycleOwner, {
                                mainRecycler.adapter = HomePageRecyclerAdapter(showsList)
                                mainRecycler.layoutManager = LinearLayoutManager(context)
                                loadingDialog.dismissAllowingStateLoss()
                                notifyChange()
                            })
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
            return root
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::viewModel.isInitialized)
            viewModel.saveState()

        super.onSaveInstanceState(outState)
    }
}

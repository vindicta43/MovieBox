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
import android.widget.TextView
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentHomePageBinding
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.utils.ToastBuilder
import com.alperen.moviebox.viewmodels.HomePageViewModel

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

        with(binding) {
            viewModel.getShows()
            loadingDialog.show(activity?.supportFragmentManager!!, "loader")
            var showList = arrayListOf<ModelShow>()

            viewModel.showList.observe(viewLifecycleOwner, { modelShowList ->
                loadingDialog.dismissAllowingStateLoss()
                showList = modelShowList
                fillSpinner(showList, spnGenres)
            })

            viewModel.getUserDetails().observeForever {
                mainRecycler.adapter = HomePageRecyclerAdapter(showList)
                mainRecycler.layoutManager = LinearLayoutManager(context)
            }

            viewModel.errorMsg.observe(viewLifecycleOwner, {
                val title = resources.getString(R.string.alert_dialog_error)
                AlertBuilder(context).build(title, it.toString())
                loadingDialog.dismissAllowingStateLoss()
            })

            etSearch.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        viewModel.getShows()
                    } else {
                        viewModel.searchShows(p0.toString())
                        viewModel.showList.observe(viewLifecycleOwner, {
                            viewModel.getUserDetails().observeForever {
                                mainRecycler.adapter = HomePageRecyclerAdapter(showList)
                                mainRecycler.layoutManager = LinearLayoutManager(context)
                            }
                        })
                    }
                }
            })

            spnGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    val textView = spnGenres.selectedView as TextView
//                    val result = textView.text.toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
            return root
        }
    }

    private fun fillSpinner(showList: ArrayList<ModelShow>, spnGenres: Spinner) {
        val genresSet = mutableSetOf<String?>()
        val all = resources.getString(R.string.home_page_spinner_first)
        genresSet.add(all)
        showList.forEach { shows ->
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
    }
}

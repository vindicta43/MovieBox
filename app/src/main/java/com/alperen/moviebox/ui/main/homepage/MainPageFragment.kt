package com.alperen.moviebox.ui.main.homepage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.alperen.moviebox.R
import com.alperen.moviebox.databinding.FragmentMainPageBinding
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.network.retrofitapi.APIService
import com.alperen.moviebox.network.retrofitapi.RetrofitInstance
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.LoadingDialog
import com.alperen.moviebox.utils.ToastBuilder
import com.alperen.moviebox.viewmodels.MainPageViewModel
import com.alperen.moviebox.viewmodels.RegisterViewModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private val loadingDialog by lazy { LoadingDialog() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this, SavedStateViewModelFactory(activity?.application, this)).get(
                MainPageViewModel::class.java
            )

        with(binding) {
            viewModel.getShows().observe(viewLifecycleOwner, { observer ->
                observeShowsResult(observer)
            })
            return root
        }
    }

    private fun observeShowsResult(result: Map<String, Any>) {
        val titleFail = resources.getString(R.string.alert_dialog_error)

        if (result.containsKey(Constants.PROCESSING)) {
            loadingDialog.show(
                activity?.supportFragmentManager!!,
                "loader"
            )
        }
        if (result.containsKey(Constants.SUCCESS)) {
            loadingDialog.dismissAllowingStateLoss()
        }
        if (result.containsKey(Constants.FAILED)) {
            loadingDialog.dismissAllowingStateLoss()
            AlertBuilder(context).build(titleFail, result[Constants.FAILED].toString())
        }
    }
}
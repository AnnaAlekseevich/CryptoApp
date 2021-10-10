package com.test.cryptoapp.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.fragments.FragmentCoinsListViewModel
import com.test.cryptoapp.net.Api

class MainFragmentViewModelFactory(private val apiService: Api) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentCoinsListViewModel::class.java)) {
            return FragmentCoinsListViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
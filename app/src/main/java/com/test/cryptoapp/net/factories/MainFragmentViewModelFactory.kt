package com.test.cryptoapp.net.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.ui.fragments.coinslist.FragmentCoinsListViewModel
import com.test.cryptoapp.net.Api

class MainFragmentViewModelFactory(private val apiService: Api) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentCoinsListViewModel::class.java)) {
            return FragmentCoinsListViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
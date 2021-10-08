package com.test.cryptoapp.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.fragments.MainFragmentViewModel
import com.test.cryptoapp.net.Api

class MainFragmentViewModelFactory(private val apiService: Api) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            return MainFragmentViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
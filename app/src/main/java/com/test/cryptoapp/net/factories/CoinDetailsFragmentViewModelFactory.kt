package com.test.cryptoapp.net.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.ui.fragments.FragmentConsDetails.FragmentCoinDetailsViewModel

class CoinDetailsFragmentViewModelFactory(private val apiService: Api): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentCoinDetailsViewModel::class.java)) {
            return FragmentCoinDetailsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.test.cryptoapp.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.crypto.CoinActivityViewModel
import com.test.cryptoapp.net.Api

class CoinActivityViewModelFactory(private val apiService: Api): ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinActivityViewModel::class.java)) {
            return CoinActivityViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
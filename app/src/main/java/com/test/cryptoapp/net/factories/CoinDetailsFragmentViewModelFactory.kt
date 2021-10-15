package com.test.cryptoapp.net.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.ui.fragments.consdetails.FragmentCoinDetailsViewModel
import kotlinx.coroutines.CoroutineDispatcher

class CoinDetailsFragmentViewModelFactory(private val api: Api, private val ioDispatcher: CoroutineDispatcher): ViewModelProvider.Factory  {
    //constructor(api: Api?) : this()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentCoinDetailsViewModel::class.java)) {
            return FragmentCoinDetailsViewModel(api, ioDispatcher) as T
//            return FragmentCoinDetailsViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
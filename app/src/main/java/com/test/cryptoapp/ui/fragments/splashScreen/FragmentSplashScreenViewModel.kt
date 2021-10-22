package com.test.cryptoapp.ui.fragments.splashScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.data.repository.coins.CoinsRepository
import kotlinx.coroutines.launch

class FragmentSplashScreenViewModel(private val coinsRepository: CoinsRepository) :
    ViewModel() {
    val coinsLiveData = MutableLiveData(false)

    init {
        viewModelScope.launch {
            val data = coinsRepository.preDownloadData(1, "market_cap_desc",20)
            coinsLiveData.value = true
        }
    }
}




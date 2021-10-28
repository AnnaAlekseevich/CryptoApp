package com.test.cryptoapp.ui.fragments.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.data.repository.coins.CoinsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val coinsRepository: CoinsRepository) :
    ViewModel() {
    private val _myUiStateSplash = MutableStateFlow(false)
    val myUiStateCoins: StateFlow<Boolean>
        get() = _myUiStateSplash

    init {
        viewModelScope.launch {
            val data = coinsRepository.preDownloadData(1, "market_cap_desc", 20)

            _myUiStateSplash.value = true
        }
    }
}




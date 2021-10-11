package com.test.cryptoapp.ui.fragments.FragmentConsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.net.models.ChartPoints
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.launch

class FragmentCoinDetailsViewModel(private val apiService: Api) : ViewModel() {

    private var vsCurrency: String = "usd"

    suspend fun reloadChart(days: String, idCoin: String): ChartPoints? {
        return apiService.getPointsForChart(id = idCoin, vsCurrency = vsCurrency, days = days).body()
    }
}
package com.test.cryptoapp.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.models.ChartPoints
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.launch

class CoinActivityViewModel(private val apiService: Api) : ViewModel() {
    var id: String = "bitcoin"
    private var vsCurrency: String = "usd"
    private var day: String = "1"

    init {
        viewModelScope.launch {
            apiService.getPointsForChart(id = id, vsCurrency = vsCurrency, days = day).body()
        }
    }
    suspend fun reloadChart(days: String, idCoin: String): ChartPoints? {
        return apiService.getPointsForChart(id = idCoin, vsCurrency = vsCurrency, days = days).body()
    }


}
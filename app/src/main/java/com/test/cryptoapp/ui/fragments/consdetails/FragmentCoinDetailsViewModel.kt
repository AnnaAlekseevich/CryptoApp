package com.test.cryptoapp.ui.fragments.consdetails

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.net.models.ChartPoints
import com.test.cryptoapp.net.models.Coin
import com.test.cryptoapp.net.models.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FragmentCoinDetailsViewModel(
    private val apiService: Api,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var daysChoose: String = "1"
    var daysChoosePercentage: String = "24h"
    var id: String = ""

    private val _myUiState = MutableStateFlow<UiState<Pair<Coin, ChartPoints>>>(UiState.Loading)
    val myUiState: StateFlow<UiState<Pair<Coin, ChartPoints>>> = _myUiState

    fun putArguments(arguments: Bundle) {
        //put all data
        requestsForCoinDetails(id, daysChoose, daysChoosePercentage)

    }

    private val latestDetailsForCharts: Flow<ChartPoints>
        get() = flow {

            Log.d("FLOW", "View Model load data")
            val latestDetails =
                apiService.getPointsForChart(id = id, vsCurrency = "usd", days = daysChoose)
            Log.d("CHECK", "daysChoose = $daysChoose")
            latestDetails?.body()?.let { emit(it) }
        }.flowOn(ioDispatcher)

    private val priceChangePercentage: Flow<Coin>
        get() = flow {
            val latestDetailsChangePercentage =
                apiService.getCoins(
                    pageNumber = null,
                    ids = id,
                    changePercentage = daysChoosePercentage,
                    sortBy = ""
                )
            Log.d("CHECK", "daysChoosePercentage = $daysChoosePercentage")
            latestDetailsChangePercentage.body()?.get(0)
                ?.let { emit(it) } // Emits the result of the request to the flow
        }.flowOn(ioDispatcher)

    fun requestsForCoinDetails(
        idCrypto: String,
        daysChooseForChart: String,
        daysChooseForPercentage: String
    ) {
        id = idCrypto
        daysChoose = daysChooseForChart
        daysChoosePercentage = daysChooseForPercentage
        _myUiState.value = UiState.Loading
        viewModelScope.launch {
            latestDetailsForCharts.zip(priceChangePercentage) { a, c ->
                Log.d("PAIR", "init data ")
                _myUiState.value = UiState.Success(Pair(c, a))
            }.collect { resp -> }
        }
    }

}

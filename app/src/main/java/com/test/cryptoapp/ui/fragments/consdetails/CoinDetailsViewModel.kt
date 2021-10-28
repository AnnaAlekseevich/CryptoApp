package com.test.cryptoapp.ui.fragments.consdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.data.repository.coins.CoinsRepository
import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinDetailsViewModel(
    private val coinRepository: CoinsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    var daysChoose: String = "1"
    var daysChoosePercentage: String = "24h"
    var id: String = ""

    private val _myUiState = MutableStateFlow<UiState<Pair<Coin, ChartPoints>>>(UiState.Loading)
    val myUiState: StateFlow<UiState<Pair<Coin, ChartPoints>>> = _myUiState

    private val latestDetailsForCharts: Flow<ChartPoints>
        get() = flow {

            val latestDetails =
                coinRepository.getPointsForChart(id = id, vsCurrency = "usd", days = daysChoose)
            latestDetails?.let { emit(it) }
        }.flowOn(ioDispatcher)

    private val priceChangePercentage: Flow<Coin>
        get() = flow {
            val latestDetailsChangePercentage =
                coinRepository.getCoins(
                    pageNumber = 0,
                    ids = id,
                    changePercentage = daysChoosePercentage,
                    sortBy = "",
                    perPage = 20,
                    isFirstLoad = false
                )
            latestDetailsChangePercentage[0]
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
                _myUiState.value = UiState.Success(Pair(c, a))
            }.collect { resp ->
            }
        }
    }

}

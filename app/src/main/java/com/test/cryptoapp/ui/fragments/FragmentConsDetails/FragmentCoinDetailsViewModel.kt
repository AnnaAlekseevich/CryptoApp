package com.test.cryptoapp.ui.fragments.FragmentConsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.data.DetailsCoinDataSource
import com.test.cryptoapp.data.repository.CryptoRepository
import com.test.cryptoapp.net.models.ChartPoints
import com.test.cryptoapp.net.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FragmentCoinDetailsViewModel(private val apiService: Api) : ViewModel() {

    private var vsCurrency: String = "usd"
    var daysChoose: String = "1"

    //val newsData: Flow<List<ChartPoints>> = DetailsCoinDataSource(apiService,days = daysChoose).flow.cachedIn(viewModelScope)


    suspend fun reloadChart(days: String, idCoin: String): ChartPoints? {
        return apiService.getPointsForChart(id = idCoin, vsCurrency = vsCurrency, days = days).body()
    }

//    init {
//        viewModelScope.launch {
//            // Trigger the flow and consume its elements using collect
//            cryptoRepository.newDetailsCoinDataSource
//                // Intermediate catch operator. If an exception is thrown,
//                // catch and update the UI
//                .catch { exception -> notifyError(exception) }
//                .collect { favoriteNews ->
//                    // Update View with the latest favorite news
//                }
//        }




    }
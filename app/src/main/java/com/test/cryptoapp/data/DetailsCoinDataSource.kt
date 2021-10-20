package com.test.cryptoapp.data

import com.test.cryptoapp.domain.net.Api
import com.test.cryptoapp.domain.models.ChartPoints
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class DetailsCoinDataSource(private val apiService: Api, var id: String, var days: String) {

    val latestDetailsForCharts: Flow<Response<ChartPoints>> = flow {
        while(true) {
            val latestDetails = apiService.getPointsForChart(id = id, vsCurrency = "usd", days = days)
            emit(latestDetails) // Emits the result of the request to the flow
        }
    }

}
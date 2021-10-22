package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin

interface RemoteCoinsDataStore {
    suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int,
        changePercentage: String?,
        ids: String?
    ): List<Coin>?

    suspend fun getPointsForChart(ids: String?, vsCurrency: String, days: String?): ChartPoints?
}
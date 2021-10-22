package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.net.Api


class RemoteCoinsDataStoreImpl(private val api: Api) : RemoteCoinsDataStore {

    override suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int,
        changePercentage: String?,
        ids: String?
    ): List<Coin>? {
        return api.getCoins(pageNumber, sortBy, perPage, changePercentage, ids).body()
    }

    override suspend fun getPointsForChart(
        ids: String?,
        vsCurrency: String,
        days: String?
    ): ChartPoints? {
        return api.getPointsForChart(ids, vsCurrency, days).body()
    }

}
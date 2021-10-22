package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin

interface CoinsRepository {

    suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int,
        changePercentage: String?,
        ids: String?,
        isFirstLoad:Boolean
    ): List<Coin>


    suspend fun getPointsForChart(id: String, vsCurrency: String, days: String): ChartPoints?

    suspend fun preDownloadData(pageNumber: Int?, sortBy: String?, perPage: Int): List<Coin>
}
package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin

class CoinsRepositoryImpl(
    private val localCoinsDataStore: LocalCoinsDataStore,
    private val remoteCoinsDataStore: RemoteCoinsDataStore
) : CoinsRepository {

    override suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int,
        changePercentage: String?,
        ids: String?,
        isFirstLoad: Boolean
    ): List<Coin> {
        return if (isFirstLoad && pageNumber == 1) {
            localCoinsDataStore.getCoins(pageNumber, sortBy, perPage)
        } else {
            remoteCoinsDataStore.getCoins(
                pageNumber,
                sortBy,
                perPage,
                changePercentage,
                ids
            )!!
        }
    }

    override suspend fun getPointsForChart(
        id: String,
        vsCurrency: String,
        days: String
    ): ChartPoints? {
        return remoteCoinsDataStore.getPointsForChart(id, vsCurrency, days)
    }

    override suspend fun preDownloadData(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int
    ): List<Coin> {
        val data = remoteCoinsDataStore.getCoins(pageNumber, sortBy, perPage, null, null)
        data?.let {
            localCoinsDataStore.deleteAll()
            localCoinsDataStore.insertAll(it)
        }
        return data!!
    }

}
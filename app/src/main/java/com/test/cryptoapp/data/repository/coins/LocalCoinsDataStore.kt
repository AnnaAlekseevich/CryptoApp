package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.models.Coin

interface LocalCoinsDataStore {
    suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int
    ): List<Coin>

    suspend fun insertAll(list: List<Coin>)

    suspend fun deleteAll()
}
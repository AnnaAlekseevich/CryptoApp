package com.test.cryptoapp.data.repository.coins

import com.test.cryptoapp.domain.db.DatabaseHelper
import com.test.cryptoapp.domain.models.Coin

class LocalCoinsDataStoreImpl(private val dbHelper: DatabaseHelper) : LocalCoinsDataStore {
    override suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int
    ): List<Coin> {
        return dbHelper.getCoins(pageNumber, sortBy, perPage)
    }

    override suspend fun insertAll(list: List<Coin>) {
        dbHelper.insertAll(list)
    }

    override suspend fun deleteAll() {
        dbHelper.deleteAll()
    }
}
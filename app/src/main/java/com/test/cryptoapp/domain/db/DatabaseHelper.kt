package com.test.cryptoapp.domain.db

import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.User

interface DatabaseHelper {
    suspend fun getCoins(
        pageNumber: Int?,
        sortBy: String?,
        perPage: Int
    ): List<Coin>

    suspend fun deleteAll()

    suspend fun insertAll(coins: List<Coin>)

    suspend fun getUser(): User

    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)


}
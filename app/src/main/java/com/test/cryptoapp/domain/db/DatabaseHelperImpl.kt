package com.test.cryptoapp.domain.db

import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.User

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getCoins(pageNumber: Int?, sortBy: String?, perPage: Int): List<Coin> {
        return appDatabase.coinDao().getCoins()
    }

    override suspend fun deleteAll() {
        appDatabase.coinDao().deleteAll()
    }

    override suspend fun insertAll(coins: List<Coin>) = appDatabase.coinDao().insertAll(coins)

    override suspend fun getUser(): User {
        return appDatabase.userDao().getUser()
    }

    override suspend fun insertUser(user: User) {
        return appDatabase.userDao().insertUser(user)
    }

    override suspend fun updateUser(userUpdate: User) {
        appDatabase.userDao().updateUser(userUpdate)
    }
}
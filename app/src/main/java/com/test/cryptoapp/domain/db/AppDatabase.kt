package com.test.cryptoapp.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.cryptoapp.domain.models.Coin
import com.test.cryptoapp.domain.models.User

@Database(entities = [Coin::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun coinDao(): CoinDao
}
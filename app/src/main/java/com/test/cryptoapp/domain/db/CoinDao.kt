package com.test.cryptoapp.domain.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.test.cryptoapp.domain.models.Coin

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin")
    fun getCoins(): List<Coin>

    @Insert
    fun insertAll(coins: List<Coin>)

    @Delete
    fun delete(coin: Coin)

    @Query("DELETE FROM coin")
    fun deleteAll()

}
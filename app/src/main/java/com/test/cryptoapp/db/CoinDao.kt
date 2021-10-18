package com.test.cryptoapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.test.cryptoapp.net.models.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Query("SELECT * FROM coin")
    suspend fun getCoins(): List<Coin>

    @Insert
    suspend fun insertAll(coins: List<Coin>)

    @Delete
    suspend fun delete(coin: Coin)

    @Query("DELETE FROM coin")
    suspend fun clearAll()

}
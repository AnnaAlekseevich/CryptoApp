package com.test.cryptoapp.db

import androidx.room.*
import com.test.cryptoapp.net.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getUser(): User

    @Insert
    suspend fun insertUser(user: User)

    @Update(entity = User::class)
    suspend fun updateUser(user: User)

}
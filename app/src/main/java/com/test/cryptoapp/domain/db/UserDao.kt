package com.test.cryptoapp.domain.db

import androidx.room.*
import com.test.cryptoapp.domain.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getUser(): User

    @Insert
    suspend fun insertUser(user: User)

    @Update(entity = User::class)
    suspend fun updateUser(user: User)

}
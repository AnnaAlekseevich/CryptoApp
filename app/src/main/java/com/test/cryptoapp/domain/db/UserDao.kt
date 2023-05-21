package com.test.cryptoapp.domain.db

import androidx.room.*
import com.test.cryptoapp.domain.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUser(): User

    @Insert
    fun insertUser(user: User)

    @Update(entity = User::class)
    fun updateUser(user: User)

}
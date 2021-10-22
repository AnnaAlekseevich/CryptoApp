package com.test.cryptoapp.data.repository.user

import com.test.cryptoapp.domain.models.User

interface UserDataStore {
    suspend fun getUser() : User
    suspend fun updateUser(user: User)
}
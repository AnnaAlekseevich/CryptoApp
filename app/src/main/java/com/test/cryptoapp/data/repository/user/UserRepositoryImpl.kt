package com.test.cryptoapp.data.repository.user

import com.test.cryptoapp.domain.models.User

class UserRepositoryImpl(private val userDataStore: UserDataStore) : UserRepository {

    override suspend fun getUser(): User {
        return userDataStore.getUser()
    }

    override suspend fun updateUser(user: User) {
        userDataStore.updateUser(user)
    }
}
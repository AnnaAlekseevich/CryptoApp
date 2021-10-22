package com.test.cryptoapp.data.repository.user

import com.test.cryptoapp.domain.db.DatabaseHelper
import com.test.cryptoapp.domain.models.User

class UserDataStoreImpl (private val dbHelper: DatabaseHelper) : UserDataStore{

    override suspend fun getUser(): User {
        return dbHelper.getUser()
    }

    override suspend fun updateUser(user: User) {
        if (dbHelper.getUser() == null) {
            dbHelper.insertUser(user)
        } else {
            dbHelper.updateUser(user)
        }
    }

}
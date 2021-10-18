package com.test.cryptoapp.data

import androidx.room.Database

@Database(entities = [Note::class, UserModel::class], version = 1, exportSchema = false)
abstract class AppDatabase {
}
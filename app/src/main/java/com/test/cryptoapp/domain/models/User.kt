package com.test.cryptoapp.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "firstName")
    val firstName: String?,
    @ColumnInfo(name = "lastName")
    val lastName: String?,
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: String?,
    @ColumnInfo(name = "authorPhotoUrl")
    val authorPhotoUrl: String?


)

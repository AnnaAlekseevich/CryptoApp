package com.test.cryptoapp.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Coin(
    //идентификатор coin
    @PrimaryKey
    @SerializedName("id")
    var cryptoId: String,

    //title description coin
    @ColumnInfo(name = "cryptoSymbol")
    @SerializedName("symbol")
    var cryptoSymbol: String?,

    //name coin
    @ColumnInfo(name = "cryptoName")
    @SerializedName("name")
    var cryptoName: String?,

    //item image coin
    @ColumnInfo(name = "urlItemCrypto")
    @SerializedName("image")
    var urlItemCrypto: String?,

    //текущая цена coin
    @ColumnInfo(name = "currentPriceCoin")
    @SerializedName("current_price")
    var currentPriceCoin: Double,

    //marcet cap coin
    @ColumnInfo(name = "marketCap")
    @SerializedName("market_cap")
    var marketCap: Double,

    //percentage
    @ColumnInfo(name = "changePercentage")
    @SerializedName("market_cap_change_percentage_24h")
    var changePercentage: String?
)
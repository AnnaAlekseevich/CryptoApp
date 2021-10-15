package com.test.cryptoapp.net.models

import com.google.gson.annotations.SerializedName

data class Coin(
    //идентификатор coin
    @SerializedName("id")
    var cryptoId: String,

    //title description coin
    @SerializedName("symbol")
    var cryptoSymbol: String?,

    //name coin
    @SerializedName("name")
    var cryptoName: String?,

    //item image coin
    @SerializedName("image")
    var urlItemCrypto: String?,

    //текущая цена coin
    @SerializedName("current_price")
    var currentPriceCoin: Double,

    //marcet cap coin
    @SerializedName("market_cap")
    var marketCap: Double,

    //percentage
    @SerializedName("market_cap_change_percentage_24h")
    var changePercentage: String?
//    "high_24h": 49785,
//"low_24h": 47188,
)
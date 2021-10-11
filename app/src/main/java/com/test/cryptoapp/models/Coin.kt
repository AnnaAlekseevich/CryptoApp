package com.test.cryptoapp.models

import com.google.gson.annotations.SerializedName

data class Coin(
    //идентификатор coin
    @SerializedName("id")
    var cryptoId: String?,

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
    var currentPriceCoin: Double
)
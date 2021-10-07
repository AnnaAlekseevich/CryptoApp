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
    var currentPriceCoin: Double?

//    //priceMarketCap coin
//    @SerializedName("market_cap")
//    var priceMarketCap:  Float? = null,
//
//    //ListChart coin
//    @SerializedName("market_cap")
//    var ListChart:  ArrayList<ChartMarket>? = null,
){
    fun getName(): String? {
        return cryptoName
    }
    fun setName(cryptoName: String) {
        this.cryptoName = cryptoName
    }
    fun getcryptoSymbol(): String? {
        return cryptoName
    }
    fun setcryptoSymbol(cryptoSymbol: String) {
        this.cryptoSymbol = cryptoSymbol
    }
    fun geturlItemCrypto(): String? {
        return urlItemCrypto
    }
    fun seturlItemCrypto(urlItemCrypto: String) {
        this.urlItemCrypto = urlItemCrypto
    }
    fun getcurrentPriceCoin(): Double? {
        return currentPriceCoin
    }
    fun setcurrentPriceCoin(currentPriceCoin: Double) {
        this.currentPriceCoin = currentPriceCoin
    }
}

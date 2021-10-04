package com.test.cryptoapp.net

import com.google.gson.annotations.SerializedName

data class CryptoResult(
    //идентификатор crypto
    @SerializedName("crypto_id")
    var cryptoId: String? = "",

    //name crypto
    var cryptoName: String? = "",

    //title description crypto
    var cryptoDescription: String? = "",

    //item crypto
    var urlItemCrypto: String? = "",

    var priceCrypto: String? = ""
)

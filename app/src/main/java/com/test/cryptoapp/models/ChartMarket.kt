package com.test.cryptoapp.models

import com.google.gson.annotations.SerializedName

data class ChartMarket(
    //самая высокая цена coin за 24ч
    @SerializedName("high_24h")
    var highPrice24h: String? = "",

    //самая низкая цена coin за 24ч
    @SerializedName("low_24h")
    var lowPrice24h: String? = "",
)

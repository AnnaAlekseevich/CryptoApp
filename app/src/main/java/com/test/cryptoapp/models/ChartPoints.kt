package com.test.cryptoapp.models

import com.google.gson.annotations.SerializedName

data class ChartPoints(
    //list Points
    @SerializedName("prices")
    var listPoints: List<List<Number>>?
)

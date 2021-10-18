package com.test.cryptoapp.net.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ChartPoints(
    //list Points
    @ColumnInfo
    @SerializedName("prices")
    var listPoints: List<List<Number>>?
)

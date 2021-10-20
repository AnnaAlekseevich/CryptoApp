package com.test.cryptoapp.domain.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ChartPoints(
    //list Points
    @ColumnInfo
    @SerializedName("prices")
    var listPoints: List<List<Number>>?
)

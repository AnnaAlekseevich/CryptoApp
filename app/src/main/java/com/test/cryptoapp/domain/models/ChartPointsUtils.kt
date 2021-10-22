package com.test.cryptoapp.domain.models

import com.github.mikephil.charting.data.Entry

fun ChartPoints?.toChartLineEntry(): List<Entry> {
    if (this == null) {
        return emptyList()
    }
    val returnList = mutableListOf<Entry>()
    listPoints?.let {externalList ->
        externalList.forEach { innerList ->
            returnList.add(Entry(innerList[0].toFloat(), innerList[1].toFloat()))
        }
    }

    return returnList
}
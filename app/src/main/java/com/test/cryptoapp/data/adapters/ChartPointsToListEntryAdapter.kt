package com.test.cryptoapp.data.adapters

import com.github.mikephil.charting.data.Entry
import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.toChartLineEntry

class ChartPointsToListEntryAdapter {

    fun convert(chartPoints : ChartPoints?) :List<Entry> = chartPoints.toChartLineEntry()

}
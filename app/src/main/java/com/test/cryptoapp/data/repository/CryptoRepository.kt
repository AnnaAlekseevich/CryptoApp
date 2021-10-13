package com.test.cryptoapp.data.repository

import com.test.cryptoapp.data.DetailsCoinDataSource
import com.test.cryptoapp.net.models.ChartPoints
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CryptoRepository(private val detailsCoinDataSource: DetailsCoinDataSource) {

//    val newDetailsCoinDataSource: Flow<List<ChartPoints>> =
//        detailsCoinDataSource.latestDetailsForCharts
//            // Intermediate operation to filter the list of favorite topics
//            .map { news -> news.filter { userData.isFavoriteTopic(it) } }
//            // Intermediate operation to save the latest news in the cache
//            //.onEach { news -> saveInCache(news) }

}
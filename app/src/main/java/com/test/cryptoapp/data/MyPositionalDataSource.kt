package com.test.cryptoapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.cryptoapp.net.models.Coin
import com.test.cryptoapp.net.Api

class MyPositionalDataSource(private val apiService: Api) :
    PagingSource<Int, Coin>() {

    var sortBy: String = "market_cap_desc"
    private var changePercentage: String = "24h"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                apiService.getCoins(currentLoadingPageKey, sortBy = sortBy, changePercentage = changePercentage)
            val pagedResponse = response.body()

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = pagedResponse ?: emptyList(),
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? {
        return state.anchorPosition
    }
}

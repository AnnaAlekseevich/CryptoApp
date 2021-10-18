package com.test.cryptoapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.cryptoapp.net.models.Coin
import com.test.cryptoapp.net.Api

class MyPositionalDataSource(private val apiService: Api, var sortingType: String) :
    PagingSource<Int, Coin>() {

    var sortBy: String = sortingType
    private var changePercentage: String = ""
    private var ids: String = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                apiService.getCoins(currentLoadingPageKey, sortBy = sortBy, changePercentage = changePercentage,ids = ids, perPage = 250)
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
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

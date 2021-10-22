package com.test.cryptoapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.cryptoapp.data.repository.coins.CoinsRepository
import com.test.cryptoapp.domain.models.Coin

class MyPositionalDataSource(
    private val coinsRepository: CoinsRepository,
    var sortingType: String,
    var isFirst: Boolean
) :
    PagingSource<Int, Coin>() {

    var sortBy: String = sortingType
    private var changePercentage: String = ""
    private var ids: String = ""
    private var perPageNumber: Int = 20


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> {
        try {
            if (isFirst == true) {
                perPageNumber = 20
            } else perPageNumber = 250

            val currentLoadingPageKey = params.key ?: 1
            val response =
                coinsRepository.getCoins(
                    currentLoadingPageKey,
                    sortBy = sortBy,
                    changePercentage = changePercentage,
                    ids = ids,
                    perPage = perPageNumber,
                    isFirstLoad = isFirst
                )
            isFirst = false
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = response,
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

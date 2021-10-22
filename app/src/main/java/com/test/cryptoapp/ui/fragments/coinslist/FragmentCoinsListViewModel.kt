package com.test.cryptoapp.ui.fragments.coinslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.cryptoapp.data.MyPositionalDataSource
import com.test.cryptoapp.data.repository.coins.CoinsRepository

class FragmentCoinsListViewModel(private val coinsRepository: CoinsRepository) : ViewModel() {

    var sortingType: String = "market_cap_desc"
    var isFirst = true

    val listData = Pager(PagingConfig(pageSize = 20)) {
        MyPositionalDataSource(coinsRepository, sortingType, isFirst)
    }.flow.cachedIn(viewModelScope)

    fun sortBy(sortBy: String) {
        sortingType = sortBy
    }

    fun onDataRefreshed(): Boolean {
        isFirst = false
        return isFirst
    }
}
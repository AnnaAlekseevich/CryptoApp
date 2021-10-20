package com.test.cryptoapp.ui.fragments.coinslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.cryptoapp.data.MyPositionalDataSource
import com.test.cryptoapp.domain.net.Api

class FragmentCoinsListViewModel(private val apiService: Api) : ViewModel() {

    var sortingType: String = "market_cap_desc"

    val listData = Pager(PagingConfig(pageSize = 20)) {
        MyPositionalDataSource(apiService, sortingType)
    }.flow.cachedIn(viewModelScope)

    fun sortBy(sortBy: String) {
        sortingType = sortBy
    }
}
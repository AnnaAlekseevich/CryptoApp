package com.test.cryptoapp.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.cryptoapp.data.MyPositionalDataSource
import com.test.cryptoapp.net.Api

class FragmentCoinsListViewModel(private val apiService: Api) : ViewModel() {

    val dataSource = MyPositionalDataSource(apiService)
    val listData = Pager(PagingConfig(pageSize = 20)) {
        dataSource
    }.flow.cachedIn(viewModelScope)

    fun sortBy(sortBy: String) {
        dataSource.sortBy = sortBy
    }
}
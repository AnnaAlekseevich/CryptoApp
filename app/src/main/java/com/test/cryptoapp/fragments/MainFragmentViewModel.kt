package com.test.cryptoapp.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.cryptoapp.data.MyPositionalDataSource
import com.test.cryptoapp.net.Api

class MainFragmentViewModel(private val apiService: Api) : ViewModel() {
    val listData = Pager(PagingConfig(pageSize = 20)) {
        MyPositionalDataSource(apiService)
    }.flow.cachedIn(viewModelScope)
}
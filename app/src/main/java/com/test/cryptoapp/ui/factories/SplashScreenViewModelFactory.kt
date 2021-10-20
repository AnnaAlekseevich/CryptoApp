package com.test.cryptoapp.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.domain.db.DatabaseHelper
import com.test.cryptoapp.domain.net.Api
import com.test.cryptoapp.ui.fragments.splashScreen.FragmentSplashScreenViewModel

class SplashScreenViewModelFactory(private val apiService: Api, private val dbHelper: DatabaseHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentSplashScreenViewModel::class.java)) {
            return FragmentSplashScreenViewModel(apiService, dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
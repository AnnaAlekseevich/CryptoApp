package com.test.cryptoapp.net.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.cryptoapp.db.DatabaseHelper
import com.test.cryptoapp.ui.fragments.settings.FragmentSettingsViewModel

class SettingFragmentViewModelFactory(private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FragmentSettingsViewModel::class.java)) {
            return FragmentSettingsViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
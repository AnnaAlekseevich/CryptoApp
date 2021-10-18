package com.test.cryptoapp.ui.fragments.FragmentSplashScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cryptoapp.db.DatabaseHelper
import com.test.cryptoapp.net.Api
import com.test.cryptoapp.net.models.Coin
import kotlinx.coroutines.launch
import retrofit2.Response

class FragmentSplashScreenViewModel(private val api: Api, private val dbHelper: DatabaseHelper) :
    ViewModel() {
    val coinsLiveData = MutableLiveData(false)

    init {
        viewModelScope.launch {
            dbHelper.clearAll()

            val coinsFromApi = api.getCoins(1, "", 20, "", "")
            Log.d("ROOMDB", "coinsFromApi = " + coinsFromApi.body())
            coinsFromApi.body()?.let { dbHelper.insertAll(it) }

            Log.d("ROOMDB", "coinsFromApi = " + coinsFromApi)

            val fgh = dbHelper.getCoins()
            coinsLiveData.value = true
            Log.d("ROOMDB", "fgh = $fgh")

        }
    }


//    fun getCoins(): LiveData<Response<List<Coin>>> {
//        return coinsLiveData
//    }
}

//    suspend fun getCoins(): List<Coin> {
//        return withContext(Dispatchers.IO) {
//            dbHelper.getCoins()
//        }
//    }




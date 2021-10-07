package com.test.cryptoapp.net

import com.google.gson.GsonBuilder
import com.test.cryptoapp.models.Coin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    /**
     *  Markets
     */

    @GET("api/v3/coins/markets?vs_currency=usd&per_page=20&page=1")
    suspend fun getCoins(@Query("page") pageNumber: Int): Response<List<Coin>>


    companion object {
        var builder = GsonBuilder()

        var gson = builder.create()
        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor();

        // set your desired log level
        var httpClient = OkHttpClient.Builder();
// add your other interceptors
// add logging as last interceptor

        fun getApiService() = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()
            .create(Api::class.java)
    }
}
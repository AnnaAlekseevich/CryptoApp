package com.test.cryptoapp.domain.net

import com.google.gson.GsonBuilder
import com.test.cryptoapp.domain.models.ChartPoints
import com.test.cryptoapp.domain.models.Coin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Api {
    /**
     *  Markets
     */

    @GET("api/v3/coins/markets?vs_currency=usd")
    suspend fun getCoins(@Query("page") pageNumber: Int?, @Query("order") sortBy: String, @Query("per_page") perPage: Int,
                         @Query("price_change_percentage") changePercentage: String, @Query("ids") ids: String): Response<List<Coin>>

    @GET("api/v3/coins/{id}/market_chart")
    suspend fun getPointsForChart(@Path("id") id: String, @Query("vs_currency") vsCurrency: String,
                                  @Query("days") days: String): Response<ChartPoints>


    companion object {
        var builder = GsonBuilder()

        var gson = builder.create()
        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()

        // set your desired log level
        var httpClient = OkHttpClient.Builder();

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
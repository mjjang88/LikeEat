package com.fund.likeeat.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object LikeEatRetrofit {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    // For Logging
    private fun okHttpClientBuilder(): OkHttpClient.Builder {
        val okhttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okhttpClientBuilder.addInterceptor(logging)

        return okhttpClientBuilder
    }

    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://likeeat-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClientBuilder().build())
            .build()
}
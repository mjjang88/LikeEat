package com.fund.likeeat.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LikeEatRetrofit {
    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://likeeat-server.firebaseio.com/map.json/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
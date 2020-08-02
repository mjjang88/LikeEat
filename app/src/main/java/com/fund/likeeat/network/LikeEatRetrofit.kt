package com.fund.likeeat.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object LikeEatRetrofit {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://likeeat-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}
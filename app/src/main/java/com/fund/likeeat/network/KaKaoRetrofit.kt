package com.fund.likeeat.network

import com.fund.likeeat.data.Place
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KaKaoRetrofit {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getService(): RetrofitKaKaoService = retrofit.create(RetrofitKaKaoService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    data class KakaoSearchPlaceResult(
        @SerializedName("documents")
        @Expose
        val documents: List<Place>
    )
}
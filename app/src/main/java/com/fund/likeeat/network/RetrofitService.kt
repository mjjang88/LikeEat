package com.fund.likeeat.network

import com.fund.likeeat.data.Place
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    @GET("/")
    fun requestPlace(
    ): Call<List<Place>>
}
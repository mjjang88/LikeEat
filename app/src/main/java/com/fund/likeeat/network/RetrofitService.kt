package com.fund.likeeat.network

import com.fund.likeeat.data.Place
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitService {

    @GET("/")
    fun requestPlace(
    ): Call<List<Place>>

    @POST("/users/login/")
    fun sendUserId(
        @Body user: User?
    ): Call<String>

}
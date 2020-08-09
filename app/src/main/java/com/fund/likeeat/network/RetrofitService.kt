package com.fund.likeeat.network

import com.fund.likeeat.data.Review
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitService {

    @GET("/")
    fun requestReview(
    ): Call<List<Review>>

    @POST("/users/login/")
    fun sendUserId(
        @Body user: User?
    ): Call<String>

}
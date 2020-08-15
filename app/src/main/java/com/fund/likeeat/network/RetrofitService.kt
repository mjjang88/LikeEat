package com.fund.likeeat.network

import com.fund.likeeat.data.Review
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitService {

    @GET("/")
    fun requestReview(
    ): Call<List<Review>>

    @POST("/users/login/")
    suspend fun sendUserInfo(
        @Body user: User?
    ): Response<String>

    // POST로 받으니 403 Forbidden (CSRF)
    // 나중에 https://likeeat-server.herokuapp.com/reviews?uid=1234567890 식으로 데이터 가져올 때 사용하면 될듯
    @GET("/reviews/")
    fun requestReviewByUid(
        /*@Query("uid") uid: Long*/
    ): Call<List<Review>>
}
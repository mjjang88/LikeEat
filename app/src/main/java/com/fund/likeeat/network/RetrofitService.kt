package com.fund.likeeat.network

import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
        @Query("uid") uid: Long
    ): Call<List<Review>>

    @GET("/reviews/")
    fun requestUserReview(
        @Query("uid") uid: Long
    ): Call<List<ReviewServerRead>>

    @POST("/reviews/")
    suspend fun addReview(
        @Body reviewNWWrite: ReviewServerWrite?
    ): Response<Unit>

    // http://likeeat-server.herokuapp.com/themes/?uid=UID
    @GET("/themes/")
    fun requestThemeByUid(
        @Query("uid") uid: Long
    ): Call<List<Theme>>

    // method: POST
    // url: http://likeeat-server.herokuapp.com/themes/
    @POST("/themes/")
    fun sendTheme(
        @Body theme: Theme
    ): Call<Theme>
}
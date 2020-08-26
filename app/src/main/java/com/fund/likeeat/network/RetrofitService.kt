package com.fund.likeeat.network

import com.fund.likeeat.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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

    @POST("/reviews/")
    suspend fun addReview(
        @Body review: Review?
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
        @Body theme: ThemeRequest
    ): Call<Theme>

    @PUT("/themes/")
    fun updateTheme(
        @Path("id") id: Long,
        @Body themeChanged: ThemeChanged
    ): Call<Theme>

    @DELETE("/themes/{id}")
    fun deleteTheme(
        @Path("id") id: Long
    ): Call<Theme>
}
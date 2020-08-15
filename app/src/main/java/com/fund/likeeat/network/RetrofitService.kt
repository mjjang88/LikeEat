package com.fund.likeeat.network

import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
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

    // POST로 받으니 403 Forbidden (CSRF)
    // 나중에 https://likeeat-server.herokuapp.com/reviews?uid=1234567890 식으로 데이터 가져올 때 사용하면 될듯
    @GET("/reviews/")
    fun requestReviewByUid(
        /*@Query("uid") uid: Long*/
    ): Call<List<Review>>

    @GET("/theme/")
    fun requestThemeByUid(
        @Field("uid") uid: Long,
        @Body theme: Theme
        /*@Field("uid") uid: Long,
        @Field("name") name: String,
        @Field("color") color: String,
        @Field("isPublic") isPublic: Boolean*/
    ): Call<String> // 무엇을 돌려받을까?
}
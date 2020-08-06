package com.fund.likeeat.network

import com.fund.likeeat.data.Place
import com.fund.likeeat.data.User
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {

    @GET("/")
    fun requestPlace(
    ): Call<List<Place>>

    @POST("/users/login/")
    fun sendUserId(
        @Body user: User?
    ): Call<String>

    // POST로 받으니 403 Forbidden (CSRF)
    // 나중에 https://likeeat-server.herokuapp.com/reviews?uid=1234567890 식으로 데이터 가져올 때 사용하면 될듯
    @GET("/reviews/")
    fun requestPlaceByUid(
        /*@Query("uid") uid: Long*/
    ): Call<List<Place>>
}
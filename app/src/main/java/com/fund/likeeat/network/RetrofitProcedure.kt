package com.fund.likeeat.network

import android.widget.Toast
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.User
import com.fund.likeeat.manager.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.KoinApplication
import org.koin.core.context.KoinContextHandler.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitProcedure {

    fun sendUserId(user: User) {
        LikeEatRetrofit.getService().sendUserId(user).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "데이터 로드 성공", Toast.LENGTH_LONG).show()
                }
            }
            /*override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                if (response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "데이터 로드 성공", Toast.LENGTH_LONG).show()
                    GlobalScope.launch {
                        response.body()?.let {
                            val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                            response.body()?.let { database.placeDao().insertAll(it) }
                        }
                    }
                }
            }*/
        })
    }

    fun getPlace() {

        LikeEatRetrofit.getService().requestPlace().enqueue(object : Callback<List<Place>> {
            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                if (response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "데이터 로드 성공", Toast.LENGTH_LONG).show()
                    GlobalScope.launch {
                        response.body()?.let {
                            val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                            response.body()?.let { database.placeDao().insertAll(it) }
                        }
                    }
                }
            }
        })
    }
}
package com.fund.likeeat.network

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.User
import com.fund.likeeat.manager.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitProcedure {

    fun getReview() {
        LikeEatRetrofit.getService().requestReview().enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "데이터 로드 성공", Toast.LENGTH_LONG).show()
                    GlobalScope.launch {
                        response.body()?.let {
                            val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                            response.body()?.let { database.reviewDao().insertAll(it) }
                        }
                    }
                }
            }
        })
    }

    fun getReviewByUid(uid: Long) {
        LikeEatRetrofit.getService().requestReviewByUid(uid).enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    GlobalScope.launch {
                        response.body()?.let {
                            val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                            response.body()?.let { database.reviewDao().insertAll(it) }
                        }
                    }
                }
            }
        })
    }

    fun getReviewByUid(uid: Long, liveData: MutableLiveData<List<Review>>?) {
        LikeEatRetrofit.getService().requestReviewByUid(uid).enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "데이터 로드 성공", Toast.LENGTH_LONG).show()

                    val list = response.body()
                    liveData?.value = list
                }
            }
        })
    }

    fun sendThemeToServer(theme: Theme) {
        LikeEatRetrofit.getService().sendTheme(theme).enqueue(object : Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {
                    Toast.makeText(MyApplication.applicationContext(), "테마 등록 완료!", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch { AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(listOf(theme)) }
                } else {
                    Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun getThemeByUid(uid: Long) {
        LikeEatRetrofit.getService().requestThemeByUid(uid).enqueue(object: Callback<List<Theme>> {
            override fun onFailure(call: Call<List<Theme>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Theme>>, response: Response<List<Theme>>) {
                if(response.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch { AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(response.body()) }
                } else {
                    Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


}
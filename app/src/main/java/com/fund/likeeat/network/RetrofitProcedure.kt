package com.fund.likeeat.network

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fund.likeeat.data.*
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.UID_DETACHED
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

    fun sendThemeToServer(theme: ThemeRequest) {
        LikeEatRetrofit.getService().sendTheme(theme).enqueue(object : Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {

                    Log.d("LOG_THEME_PK", "pk : " + response.body()?.pk?.toString())
                    Log.d("LOG_THEME_UID", "uid : " + response.body()?.uid?.toString())
                    Log.d("LOG_THEME_NAME", "name : " + response.body()?.name)
                    Log.d("LOG_THEME_COLOR", "color : " + response.body()?.color?.toString())
                    Log.d("LOG_THEME_ISPUBLIC", "isPublic : " + response.body()?.isPublic)

                    Toast.makeText(MyApplication.applicationContext(), "테마 등록 완료!", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(
                            listOf(
                                Theme(
                                    response.body()?.pk!!,
                                    response.body()?.pk!!,
                                    response.body()?.uid!!,
                                    response.body()?.reviewsCount!!,
                                    theme.name,
                                    theme.color,
                                    theme.isPublic
                                )
                            )
                        )
                        // AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(listOf(response.body()!!))
                    }
                } else {
                    Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun getThemeByUid(uid: Long) {
        if(MyApplication.pref.uid == UID_DETACHED) return

        LikeEatRetrofit.getService().requestThemeByUid(uid).enqueue(object: Callback<List<Theme>> {
            override fun onFailure(call: Call<List<Theme>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Theme>>, response: Response<List<Theme>>) {
                if(response.isSuccessful) {

                    Log.d("LOG_THEME_UID", "넘겨준 UID : " + uid.toString())
                    for(theme in response.body()!!) {
                        Log.d("LOG_THEME_PK", "pk : " + theme.id.toString())
                        Log.d("LOG_THEME_UID", "uid : " + theme.uid.toString())
                        Log.d("LOG_THEME_NAME", "name : " + theme.name)
                        Log.d("LOG_THEME_COLOR", "color : " + theme.color.toString())
                        Log.d("LOG_THEME_ISPUBLIC", "isPublic : " + theme.isPublic.toString())
                    }

                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(response.body())
                    }
                } else {
                    Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun updateThemeById(id: Long, themeChanged: ThemeChanged) {
        LikeEatRetrofit.getService().updateTheme(id, themeChanged).enqueue(object :Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 수정 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().updateTheme(id, themeChanged.name, themeChanged.color, themeChanged.isPublic)
                    }
                    Toast.makeText(MyApplication.applicationContext(), "테마 수정 완료", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun deleteThemeById(id: Long) {
        LikeEatRetrofit.getService().deleteTheme(id).enqueue(object: Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 삭제 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().deleteTheme(id)
                    }
                    Toast.makeText(MyApplication.applicationContext(), "테마 삭제 완료", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

}
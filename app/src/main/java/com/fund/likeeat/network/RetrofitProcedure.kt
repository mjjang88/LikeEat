package com.fund.likeeat.network

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fund.likeeat.data.*
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.ToastUtil
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

    fun sendThemeToServer(activity: Activity, theme: ThemeRequest) {
        LikeEatRetrofit.getService().sendTheme(theme).enqueue(object : Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful)  {
                    ToastUtil.toastShort(activity, "테마를 등록했습니다")
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(
                            listOf(
                                Theme(
                                    response.body()?.id!!,
                                    response.body()?.uid!!,
                                    response.body()?.reviewsCount!!,
                                    theme.name,
                                    theme.color,
                                    theme.isPublic
                                )
                            )
                        )
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
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().insertTheme(response.body())
                    }
                } else {
                    Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun updateThemeById(activity: Activity, id: Long, themeChanged: ThemeChanged) {
        LikeEatRetrofit.getService().updateTheme(id, themeChanged).enqueue(object :Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 수정 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().updateTheme(id, themeChanged.name, themeChanged.color, themeChanged.isPublic)
                    }
                    ToastUtil.toastShort(activity, "테마 수정을 완료했습니다")
                }
            }

        })
    }

    fun deleteThemeById(activity: Activity, id: Long) {
        LikeEatRetrofit.getService().deleteTheme(id).enqueue(object: Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 삭제 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful) {
                    GlobalScope.launch {
                        AppDatabase.getInstance(MyApplication.applicationContext()).themeDao().deleteTheme(id)
                    }
                    ToastUtil.toastShort(activity, "테마를 삭제했습니다")
                }
            }

        })
    }

}
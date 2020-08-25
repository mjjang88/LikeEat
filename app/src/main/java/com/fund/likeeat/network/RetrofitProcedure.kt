package com.fund.likeeat.network

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fund.likeeat.data.*
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

    fun getUserReview(uid: Long) {
        LikeEatRetrofit.getService().requestUserReview(uid).enqueue(object : Callback<List<ReviewServerRead>> {
            override fun onFailure(call: Call<List<ReviewServerRead>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "데이터 로드 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<ReviewServerRead>>, response: Response<List<ReviewServerRead>>) {
                if (response.isSuccessful) {
                    GlobalScope.launch {
                        response.body()?.let {

                            val reviewList = ArrayList<Review>()
                            val reviewThemeLinkList = ArrayList<ReviewThemeLink>()

                            for (item in it) {
                                Review(
                                    item.id,
                                    item.uid,
                                    item.isPublic,
                                    item.category,
                                    item.comment,
                                    item.visitedDayYmd,
                                    item.companions,
                                    item.toliets,
                                    item.priceRange,
                                    item.serviceQuality,
                                    item.revisit,
                                    null,
                                    item.place?.lng,
                                    item.place?.lat,
                                    item.place?.name,
                                    item.place?.address,
                                    item.place?.phoneNumber
                                ).let { review -> reviewList.add(review) }

                                for (theme in item.themes) {
                                    ReviewThemeLink(
                                        item.id,
                                        theme.id
                                    ).let { reviewThemeLink -> reviewThemeLinkList.add(reviewThemeLink) }
                                }
                            }

                            val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                            database.reviewDao().deleteAndInsertAll(reviewList)
                            database.reviewThemeLinkDao().deleteAndInsertAll(reviewThemeLinkList)
                        }
                    }
                }
            }
        })
    }

    fun getUserReview(uid: Long, liveData: MutableLiveData<List<Review>>?) {
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
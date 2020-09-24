package com.fund.likeeat.network

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fund.likeeat.R
import com.fund.likeeat.data.*
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.ThemeType
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.utilities.UpdateReviewOnlyThemeType
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

    fun sendThemeToServer(theme: ThemeRequest, type: ThemeType) {
        LikeEatRetrofit.getService().sendTheme(theme).enqueue(object : Callback<Theme> {
            override fun onFailure(call: Call<Theme>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 저장 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Theme>, response: Response<Theme>) {
                if(response.isSuccessful)  {
                    if(type == ThemeType.TYPE_CUSTOM_THEME) { ToastUtil.toastShort("테마를 등록했습니다") }
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

    // 만약 getThemeByUid를 통해 데이터를 불러올 경우, 값이 하나도 안들어있다면 기본 Default 테마를 만들어준다.
    // 그 이후에는 무조건 테마가 하나 이상 존재하게 되므로, 또 다시 Default 테마가 추가 될 일은 없다. (서버 자체에서 삭제하지 않는 이상은)
    fun getThemeByUid(uid: Long) {
        if(MyApplication.pref.uid == UID_DETACHED) return

        LikeEatRetrofit.getService().requestThemeByUid(uid).enqueue(object: Callback<List<Theme>> {
            override fun onFailure(call: Call<List<Theme>>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "테마 로드 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Theme>>, response: Response<List<Theme>>) {
                if(response.isSuccessful) {
                    if (response.body().isNullOrEmpty()) {
                        val theme = ThemeRequest(
                            uid,
                            MyApplication.applicationContext().resources.getString(R.string.theme_all),
                            Color.BLACK,
                            true
                        )
                        sendThemeToServer(theme, ThemeType.TYPE_FIRST_THEME)
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            AppDatabase.getInstance(MyApplication.applicationContext()).themeDao()
                                .insertTheme(
                                    response.body()?.map { it.copy(uid = uid) })
                        }
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
                    ToastUtil.toastShort("테마 수정을 완료했습니다")
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
                    ToastUtil.toastShort("테마를 삭제했습니다")
                }
            }

        })
    }

    fun updateReviewOnlyTheme(reviewId: Long, themeId: Long, changeRequest: ReviewChanged, type: UpdateReviewOnlyThemeType, newThemeId: Long = -1L) {
        LikeEatRetrofit.getService().updateReviewOnlyTheme(reviewId, changeRequest).enqueue(object: Callback<Review> {
            override fun onFailure(call: Call<Review>, t: Throwable) {
                Toast.makeText(MyApplication.applicationContext(), "맛집 수정 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Review>, response: Response<Review>) {
                if(response.isSuccessful) {
                    when(type) {
                        UpdateReviewOnlyThemeType.TYPE_DELETE -> {
                            GlobalScope.launch {
                                AppDatabase.getInstance(MyApplication.applicationContext()).reviewThemeLinkDao().deleteOneRelation(reviewId, themeId)
                            }
                            ToastUtil.toastShort("맛집을 테마에서 제거했습니다")
                        }
                        UpdateReviewOnlyThemeType.TYPE_MOVE -> {
                            GlobalScope.launch {
                                AppDatabase.getInstance(MyApplication.applicationContext()).reviewThemeLinkDao().deleteOneRelation(reviewId, newThemeId)
                                AppDatabase.getInstance(MyApplication.applicationContext()).reviewThemeLinkDao().updateOneRelation(reviewId, themeId, newThemeId)
                            }
                            ToastUtil.toastShort("테마를 이동했습니다")
                        }
                    }
                    getThemeByUid(MyApplication.pref.uid)
                    getUserReview(MyApplication.pref.uid)
                }
            }

        })
    }

}
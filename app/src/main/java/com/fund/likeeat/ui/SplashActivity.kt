package com.fund.likeeat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.DataUtils
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.friends.AppFriendContext
import com.kakao.friends.AppFriendOrder
import com.kakao.friends.response.AppFriendsResponse
import com.kakao.kakaotalk.callback.TalkResponseCallback
import com.kakao.kakaotalk.v2.KakaoTalkService
import com.kakao.network.ErrorResult
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        AuthService.getInstance()
            .requestAccessTokenInfo(kakaoApiResponseCallback)
    }

    val kakaoApiResponseCallback = object : ApiResponseCallback<AccessTokenInfoResponse?>() {
        override fun onSessionClosed(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")

            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }

        override fun onFailure(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "토큰 정보 요청 실패: $errorResult")
        }

        override fun onSuccess(result: AccessTokenInfoResponse?) {
            Log.i("KAKAO_API", "사용자 아이디: " + result?.userId)
            Log.i("KAKAO_API", "남은 시간(s): " + result?.expiresIn)

            lifecycleScope.launch(Dispatchers.IO) {
                result?.let {
                    DataUtils.attachMyUid(result.userId)
                    RetrofitProcedure.getThemeByUid(MyApplication.pref.uid)
                    RetrofitProcedure.getUserReview(MyApplication.pref.uid)
                    getKakaoFriends()
                    RetrofitProcedure.getFriends()
                }

                withContext(Dispatchers.Main) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }
}

suspend fun getKakaoFriends() {
    var bEndDbSave = false

    val context = AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc")

    KakaoTalkService.getInstance()
        .requestAppFriends(context, object : TalkResponseCallback<AppFriendsResponse>() {
            override fun onSuccess(result: AppFriendsResponse?) {

                val friends: ArrayList<KakaoFriend> = ArrayList()
                result?.friends?.forEach {
                    val kakaoFriend = KakaoFriend(
                        it.id,
                        it.profileNickname,
                        it.profileThumbnailImage
                    )
                    friends.add(kakaoFriend)
                }

                runBlocking {
                    val job = GlobalScope.launch(Dispatchers.IO) {
                        val database : AppDatabase = AppDatabase.getInstance(MyApplication.applicationContext())
                        database.kakaoFriendDao().deleteAndInsertAll(friends)
                    }
                    job.join()
                    bEndDbSave = true
                }
            }

            override fun onNotKakaoTalkUser() {
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
            }

            override fun onFailure(errorResult: ErrorResult?) {
            }
        })

    while (!bEndDbSave) {
        delay(500)
    }
}
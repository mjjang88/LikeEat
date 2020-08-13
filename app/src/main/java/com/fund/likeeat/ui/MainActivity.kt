package com.fund.likeeat.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.fund.likeeat.R
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.User
import com.fund.likeeat.databinding.ActivityMainBinding
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.DataUtils
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.util.helper.Utility


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        val keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash)
        AuthService.getInstance()
            .requestAccessTokenInfo(kakaoApiResponseCallback)


        AsyncTask.execute {
            for(i in 0 until 5) {
                AppDatabase.getInstance(this).reviewDao().insertReview(
                    Review(
                        i.toLong(),
                        12331233,
                        "최강피자$i",
                        "서울 서초구 태봉로 2길 60 (양재동)",
                        "맛있다",
                        123.123 + (10 - i),
                        31.21,
                        "https://www.hapskorea.com/wp-content/uploads/2019/08/black-angus-lobster-ball-pizza.jpg"
                    )
                )
            }}
    }

    val kakaoApiResponseCallback = object : ApiResponseCallback<AccessTokenInfoResponse?>() {
        override fun onSessionClosed(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")

            startLoginActivity()
        }

        override fun onFailure(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "토큰 정보 요청 실패: $errorResult")
        }

        override fun onSuccess(result: AccessTokenInfoResponse?) {
            Log.i("KAKAO_API", "사용자 아이디: " + result?.userId)
            Log.i("KAKAO_API", "남은 시간(s): " + result?.expiresIn)

            result?.let {
                RetrofitProcedure.sendUserId(User(it.userId))
                DataUtils.attachMyUid(result.userId)
            }
        }
    }

    private fun startLoginActivity() {
        intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
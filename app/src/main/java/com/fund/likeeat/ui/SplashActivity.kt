package com.fund.likeeat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.DataUtils
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult

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

            result?.let {
                DataUtils.attachMyUid(result.userId)
                RetrofitProcedure.getThemeByUid(MyApplication.pref.uid)
                RetrofitProcedure.getUserReview(MyApplication.pref.uid)
            }

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
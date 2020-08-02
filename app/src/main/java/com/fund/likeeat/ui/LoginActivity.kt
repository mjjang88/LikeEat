package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.data.User
import com.fund.likeeat.databinding.ActivityMainBinding
import com.fund.likeeat.network.RetrofitProcedure
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.util.exception.KakaoException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_login
        )

        Session.getCurrentSession().addCallback(sessionCallback)

        AuthService.getInstance()
            .requestAccessTokenInfo(kakaoApiResponseCallback)
    }

    // 세션 콜백 구현
    val sessionCallback: ISessionCallback = object: ISessionCallback {
        override fun onSessionOpened() {
            Log.i("KAKAO_SESSION", "로그인 성공")

            AuthService.getInstance()
                .requestAccessTokenInfo(kakaoApiResponseCallback)
        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            Log.e("KAKAO_SESSION", "로그인 실패", exception)
        }
    }

    val kakaoApiResponseCallback = object : ApiResponseCallback<AccessTokenInfoResponse?>() {
        override fun onSessionClosed(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
        }

        override fun onFailure(errorResult: ErrorResult) {
            Log.e("KAKAO_API", "토큰 정보 요청 실패: $errorResult")
        }

        override fun onSuccess(result: AccessTokenInfoResponse?) {
            Log.i("KAKAO_API", "사용자 아이디: " + result?.userId)
            Log.i("KAKAO_API", "남은 시간(s): " + result?.expiresIn)

            result?.let {
                RetrofitProcedure.sendUserId(User(it.userId))
            }

            startMainActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}
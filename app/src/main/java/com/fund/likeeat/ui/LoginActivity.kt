package com.fund.likeeat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fund.likeeat.R
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException

class LoginActivity : AppCompatActivity() {

    // 세션 콜백 구현
    val sessionCallback: ISessionCallback = object: ISessionCallback {
        override fun onSessionOpened() {
            Log.i("KAKAO_SESSION", "로그인 성공");
        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            Log.e("KAKAO_SESSION", "로그인 실패", exception);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Session.getCurrentSession().addCallback(sessionCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
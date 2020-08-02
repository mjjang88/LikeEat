package com.fund.likeeat.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.fund.likeeat.R
import com.fund.likeeat.data.User
import com.fund.likeeat.databinding.ActivityMainBinding
import com.fund.likeeat.network.RetrofitProcedure
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )

        AuthService.getInstance()
            .requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse?>() {
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                }

                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "토큰 정보 요청 실패: $errorResult")
                }

                override fun onSuccess(result: AccessTokenInfoResponse?) {
                    result?.let {
                        RetrofitProcedure.sendUserId(User(it.userId))
                    }
                    Log.i("KAKAO_API", "사용자 아이디: " + result?.userId)
                    Log.i("KAKAO_API", "남은 시간(s): " + result?.expiresIn)
                }
            })

        /*UserManagement.getInstance().me(object: MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                val kakaoAccount = result?.kakaoAccount
                if(kakaoAccount != null) {
                    val email = kakaoAccount.email
                    if(email != null) {
                        Log.i("KAKAO_API", "email: $email")
                    }
                }


            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
            }

        })*/

        loadData()
    }

    fun loadData() {
        RetrofitProcedure.getPlace()
    }
}
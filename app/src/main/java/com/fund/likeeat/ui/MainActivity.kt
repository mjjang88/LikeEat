package com.fund.likeeat.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.R
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceRepository
import com.fund.likeeat.data.User
import com.fund.likeeat.databinding.ActivityMainBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.DataUtils
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.util.helper.Utility
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


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

        // 테스트용.. (삭제)
        AsyncTask.execute { AppDatabase.getInstance(this).placeDao().insertPlace(Place(333333, "comment1", 333.212, 1.333)) }
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
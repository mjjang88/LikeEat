package com.fund.likeeat.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fund.likeeat.R
import com.fund.likeeat.utilities.DataUtils
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        try {
            val pInfo: PackageInfo = applicationContext.packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            app_version.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        action_back.setOnClickListener { finish() }

        /*layout_logout.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("KAKAO_LOGOUT", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i("KAKAO_LOGOUT", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    DataUtils.detachMyUid()
                    application.deleteDatabase("like-eat-db")

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }*/
    }
}
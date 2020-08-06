package com.fund.likeeat.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    val pref: SharedPreferences = context.getSharedPreferences("pref", 0)

    /*
    어디서든 사용할 수 있는 나의 uid
    LoginActivity로 이동하면 UID_DETACHED 부여
    KakaoCallback 성공하면 uid 값 넣어줌
     */
    var uid: Long
        get() = pref.getLong("uid", UID_DETACHED)
        set(value) = pref.edit().putLong("uid", value).apply()
}
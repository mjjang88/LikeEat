package com.fund.likeeat.utilities

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {
    val pref: SharedPreferences = context.getSharedPreferences("pref", 0)

    var uid: Long
        get() = pref.getLong("uid", UID_DETACHED)
        set(value) = pref.edit().putLong("uid", value).apply()

    var isNeedToSeeOnBoarding: Boolean
        get() = pref.getBoolean("needToSeeOnBoarding", true)
        set(value) = pref.edit().putBoolean("needToSeeOnBoarding", value).apply()
}
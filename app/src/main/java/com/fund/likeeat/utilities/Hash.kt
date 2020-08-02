package com.fund.likeeat.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

class Hash(val context: Context) {
    fun getAppKeyHash() {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for(i in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(i.toByteArray())

                val something = String(Base64.encode(md.digest(), 0)!!)
                Log.e("Hash key", something)
            }
        } catch(e: Exception) {
            Log.e("name not found", e.toString())
        }
    }
}
package com.fund.likeeat.utilities

import android.location.Location
import com.fund.likeeat.manager.MyApplication

object DataUtils {
    fun detachMyUid() {
        MyApplication.pref.uid = UID_DETACHED
    }

    fun attachMyUid(uid: Long) {
        MyApplication.pref.uid = uid
    }

    fun distanceByDegree(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val start = Location("A")
        val end = Location("B")

        start.latitude = lat1
        start.longitude = lon1
        end.latitude = lat2
        end.longitude = lon2

        return start.distanceTo(end).toDouble()
    }
}
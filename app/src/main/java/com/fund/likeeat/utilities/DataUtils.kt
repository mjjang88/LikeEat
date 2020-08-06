package com.fund.likeeat.utilities

import com.fund.likeeat.manager.MyApplication

object DataUtils {
    fun detachMyUid() {
        MyApplication.pref.uid = UID_DETACHED
    }

    fun attachMyUid(uid: Long) {
        MyApplication.pref.uid = uid
    }
}
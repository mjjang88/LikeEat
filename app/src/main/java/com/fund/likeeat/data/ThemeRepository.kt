package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ThemeRepository (
    private val themeDao: ThemeDao
){
    fun getThemeList(uid: Long): LiveData<List<Theme>> {
        return themeDao.getThemeList(uid)
    }
}
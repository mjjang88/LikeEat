package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ThemeRepository (
    private val themeDao: ThemeDao
){
    fun getTheme(id: Long) = themeDao.getTheme(id)

    fun getThemeList(): LiveData<MutableList<Theme>> {
        return themeDao.getThemeList()
    }

    fun getThemeList2(uid: Long): List<Theme> {
        return themeDao.getThemeList2(uid)
    }
}
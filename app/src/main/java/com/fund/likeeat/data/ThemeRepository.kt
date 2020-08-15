package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ThemeRepository (
    private val themeDao: ThemeDao
){
    fun getThemeList(): LiveData<List<Theme>> {
        return themeDao.getThemeList()
    }
}
package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRepository

class ThemeViewModel internal constructor(
    themeRepository: ThemeRepository,
    uid: Long
): ViewModel() {
    val themeList: LiveData<List<Theme>> = themeRepository.getThemeList()
}
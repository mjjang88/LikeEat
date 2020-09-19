package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRepository

class AllThemesViewModel internal constructor(
    themeRepository: ThemeRepository,
    uid: Long
): ViewModel() {
    val themeList: LiveData<MutableList<Theme>> = themeRepository.getThemeList()
}
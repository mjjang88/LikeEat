package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRepository

class AddReviewViewModel internal constructor(
    val themeRepository: ThemeRepository,
    val uid: Long
): ViewModel() {
    fun getThemeList(): List<Theme> {
        return themeRepository.getThemeList2(uid)
    }
}
package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ThemeRepository

class SetThemeBottomSheetViewModel(
    themeRepository: ThemeRepository,
    themeId: Long
): ViewModel() {
    val theme = themeRepository.getTheme(themeId)
}
package com.fund.likeeat.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ThemeChanged
import com.fund.likeeat.data.ThemeRepository
import com.fund.likeeat.network.RetrofitProcedure

class OneThemeViewModel(
    themeRepository: ThemeRepository,
    themeId: Long
): ViewModel() {
    val theme = themeRepository.getTheme(themeId)

    fun updateTheme(
        activity: Activity,
        id: Long,
        name: String = theme.value!!.name,
        color: Int = theme.value!!.color,
        isPublic: Boolean = theme.value!!.isPublic
    ) {
        RetrofitProcedure.updateThemeById(
            id,
            ThemeChanged(name, color, isPublic)
        )
    }
}
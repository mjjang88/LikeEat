package com.fund.likeeat.ui

import android.os.Bundle
import com.fund.likeeat.data.ThemeRequest
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure

class AddThemeActivity : SetThemeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.screenTitle.text = "새로운 테마 추가"

        binding.actionEnroll.setOnClickListener {
            if (verifyThemeName(binding.themeName.text.toString())) {
                addNewTheme()
                finish()
            }
        }
    }

    private fun addNewTheme() {
        val theme = ThemeRequest(
            MyApplication.pref.uid,
            binding.themeName.text.toString(),
            colorSelected,
            isPublic
        )
        RetrofitProcedure.sendThemeToServer(theme)
    }
}
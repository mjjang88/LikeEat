package com.fund.likeeat.ui

import android.os.Bundle
import com.fund.likeeat.R
import com.fund.likeeat.data.ThemeRequest
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.utilities.ThemeType
import com.naver.maps.geometry.LatLng

class AddThemeActivity : SetThemeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        naverMapInfo = intent.getParcelableExtra<LatLng>(INTENT_KEY_LOCATION)
        binding.screenTitle.text = resources.getString(R.string.title_add_new_theme)

        binding.actionEnroll.setOnClickListener {
            if (isCorrectInputInformation) {
                addNewTheme()
                finish()
            }
        }
    }

    private fun addNewTheme() {
        val theme = ThemeRequest(
            MyApplication.pref.uid,
            binding.themeName.text.toString(),
            colorSelected!!,
            isPublic
        )
        RetrofitProcedure.sendThemeToServer(theme, ThemeType.TYPE_CUSTOM_THEME)
    }
}
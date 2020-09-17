package com.fund.likeeat.ui

import android.os.Bundle
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.data.ThemeRequest
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import kotlinx.android.synthetic.main.activity_set_theme.*

class UpdateThemeActivity : SetThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.screenTitle.text = "테마 수정"

        themeViewModel.theme.observe(this) { theme ->
            binding.themeName.setText(theme.name)

            colorSelected = theme.color
            binding.themeColor.circleColor = colorSelected

            if(theme.isPublic) {
                isPublic = true
                theme_is_public.text = "공개"
                image_public.setImageResource(R.drawable.ic_eye_24)
            } else {
                isPublic = false
                theme_is_public.text = "비공개"
                image_public.setImageResource(R.drawable.ic_eye_off_24)
            }
        }

        binding.actionEnroll.setOnClickListener {
            if(verifyThemeName(binding.themeName.text.toString())) {
                updateTheme()
                finish()
            }
        }
    }


    private fun updateTheme() {
        themeViewModel.updateTheme(
            themeId!!,
            name = binding.themeName.text.toString(),
            color = colorSelected,
            isPublic = isPublic
        )
    }
}
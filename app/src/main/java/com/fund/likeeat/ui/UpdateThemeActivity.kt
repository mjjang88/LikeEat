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

        binding.screenTitle.text = resources.getString(R.string.title_edit_theme)

        themeViewModel.theme.observe(this) { theme ->
            binding.themeName.setText(theme.name)

            colorSelected = theme.color
            binding.themeColor.circleColor = colorSelected

            if(theme.isPublic) {
                isPublic = true
                theme_is_public.text = resources.getString(R.string.theme_public_open)
                image_public.setImageResource(R.drawable.ic_eye_24)
            } else {
                isPublic = false
                theme_is_public.text = resources.getString(R.string.theme_public_close)
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
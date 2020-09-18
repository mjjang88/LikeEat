package com.fund.likeeat.ui

import android.os.Bundle
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.utilities.ColorList
import kotlinx.android.synthetic.main.activity_set_theme.*

class UpdateThemeActivity : SetThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.screenTitle.text = resources.getString(R.string.title_edit_theme)

        themeViewModel.theme.observe(this) { theme ->
            val list = ColorList.colorList.filter { it.first == theme.color }
            val colorText = list[0].second

            binding.themeName.setText(theme.name)

            colorSelected = theme.color
            binding.themeTag.setColorFilter(colorSelected)

            binding.themeColorText.text = colorText

            if(theme.isPublic) {
                isPublic = true
                theme_is_public.text = resources.getString(R.string.theme_public_open)
                image_public.setImageResource(R.drawable.ic_baseline_visibility_24)
            } else {
                isPublic = false
                theme_is_public.text = resources.getString(R.string.theme_public_close)
                image_public.setImageResource(R.drawable.ic_baseline_visibility_off_24)
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
            this,
            themeId!!,
            name = binding.themeName.text.toString(),
            color = colorSelected,
            isPublic = isPublic
        )
    }
}
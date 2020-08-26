package com.fund.likeeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivityChangeThemeNameBinding
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.OneThemeViewModel
import kotlinx.android.synthetic.main.activity_change_theme_name.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChangeThemeNameActivity : AppCompatActivity() {
    var themeId: Long? = null
    private lateinit var binding: ActivityChangeThemeNameBinding
    private val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeId = intent.getLongExtra("THEME_ID", UID_DETACHED)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_theme_name)
        binding.viewModel = themeViewModel
        binding.lifecycleOwner = this

        btn_apply.setOnClickListener {
            themeViewModel.updateTheme(themeId!!, name = theme_name_changed.text.toString())
            finish()
        }
    }
}
package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ThemeAdapter
import com.fund.likeeat.databinding.ActivitySetThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SetThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetThemeBinding
    private val themeViewModel: ThemeViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySetThemeBinding>(this, R.layout.activity_set_theme)
        binding.lifecycleOwner = this

        val adapter = ThemeAdapter(supportFragmentManager)
        binding.recycler.adapter = adapter
        subscribeUi(adapter)

        binding.themeAdd.setOnClickListener {
            startActivity(Intent(this@SetThemeActivity, AddThemeActivity::class.java))
        }
    }

    private fun subscribeUi(adapter: ThemeAdapter) {
        themeViewModel.themeList.observe(this) { result ->
            adapter.submitList(result)
        }
    }
}
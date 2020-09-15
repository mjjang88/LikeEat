package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.OnClickAddThemeListener
import com.fund.likeeat.adapter.ThemeAdapter
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivitySetThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.AllThemesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SetThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetThemeBinding
    private val themeViewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySetThemeBinding>(this, R.layout.activity_set_theme)
        binding.lifecycleOwner = this

        val adapter = ThemeAdapter(supportFragmentManager, object: OnClickAddThemeListener {
            override fun onClick() {
                startActivity(Intent(this@SetThemeActivity, AddThemeActivity::class.java))
            }
        })

        binding.recycler.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: ThemeAdapter) {
        themeViewModel.themeList.observe(this) { result ->
            // TODO 의미없는 데이터입니다. 처리를 해야 할 필요가 있습니다.
            result.add(0, Theme(-11,-11,-11,"d",-11,true))
            result.add(Theme(-11,-11,-11,"d",-11,true))
            adapter.submitList(result)
        }
    }
}
package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.OnClickAddThemeListener
import com.fund.likeeat.adapter.OnClickCardListener
import com.fund.likeeat.adapter.ThemeAdapter
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.viewmodels.AllThemesViewModel
import com.fund.likeeat.viewmodels.ReviewThemeLinkViewModel
import com.fund.likeeat.widget.OnSelectEditListener
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemeBinding
    private val themeViewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityThemeBinding>(this, R.layout.activity_theme)
        binding.lifecycleOwner = this

        val naverMapInfo = intent.getParcelableExtra<LatLng>(INTENT_KEY_LOCATION)

        val adapter = ThemeAdapter(supportFragmentManager).apply {
            setOnAddThemeListener(object: OnClickAddThemeListener {
                override fun onClick() { startActivity(Intent(this@ThemeActivity, AddThemeActivity::class.java)
                    .apply{ putExtra(INTENT_KEY_LOCATION, naverMapInfo) }) }
            })

            setOnClickCardListener(object: OnClickCardListener {
                override fun onClick(themeId: Long, themeName: String) {
                    val intent = Intent(this@ThemeActivity, PlacesInThemeActivity::class.java)
                    intent.putExtra("THEME_ID", themeId)
                    intent.putExtra("THEME_NAME", themeName)
                    intent.putExtra(INTENT_KEY_LOCATION, naverMapInfo)
                    startActivity(intent)
                }
            })

            setOnSelectEditListener(object: OnSelectEditListener {
                override fun onSelectEdit(themeId: Long?) {
                    val intent = Intent(this@ThemeActivity, UpdateThemeActivity::class.java).apply {
                        putExtra(INTENT_KEY_LOCATION, naverMapInfo)
                        putExtra("THEME_ID", themeId)
                    }
                    startActivity(intent)
                }
            })
        }

        binding.recycler.adapter = adapter

        themeViewModel.themeList.observe(this) { result ->
            // TODO 의미없는 데이터입니다. 처리를 해야 할 필요가 있습니다.
            result.add(0, Theme(-11,-11,-11,"d",-11,true))
            result.add(Theme(-11,-11,-11,"d",-11,true))
            adapter.submitList(result)

            /*val themeIdList = result.map{ it.id }
            GlobalScope.launch { themeViewModel.getAllRelations(themeIdList) }*/
        }

        /*themeViewModel.allRelations.observe(this) {

        }*/



        binding.actionBack.setOnClickListener { finish() }
    }
}
package com.fund.likeeat.ui

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivitySetThemeBinding
import com.fund.likeeat.utilities.ColorList
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.fund.likeeat.widget.ThemeColorSelectBottomSheetFragment
import kotlinx.android.synthetic.main.activity_set_theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class SetThemeActivity : AppCompatActivity() {
    var themeId: Long? = null
    var isPublic = true
    var colorSelected: Int = Color.BLACK

    var isFocusingEditText = false

    val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }
    lateinit var binding: ActivitySetThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeId = intent.getLongExtra("THEME_ID", UID_DETACHED)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_theme)
        binding.apply {
            lifecycleOwner = this@SetThemeActivity

            layoutTag.setOnClickListener { openColorSheetAndSetThemeColor() }
            themeName.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    theme_name.setBackgroundResource(R.drawable.item_border_round_primary100)
                    isFocusingEditText = true
                }
            }
            themeName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(result: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!verifyThemeName(result.toString())) {
                        action_enroll.setBackgroundResource(R.drawable.item_fill_black05_round)
                    } else {
                        action_enroll.setBackgroundResource(R.drawable.item_fill_black01_round)
                    }
                }
            })

            layoutThemeSetPublic.setOnClickListener {
                if (isPublic) {
                    themeIsPublic.text = resources.getString(R.string.theme_public_close)
                    imagePublic.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                } else {
                    themeIsPublic.text = resources.getString(R.string.theme_public_open)
                    imagePublic.setImageResource(R.drawable.ic_baseline_visibility_24)
                }
                isPublic = !isPublic
            }

            actionBack.setOnClickListener { finish() }
        }
    }

    // TODO 테마 이름을 입력 하다가 다른 곳으로 Focusing 됐을 때, 
    //  특정 경우에 Focusing이 해제 되지 않는 이슈 (+ border 색도 변하지 않음)
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null && isFocusingEditText) {
            val area = Rect()
            focusView.getGlobalVisibleRect(area)    // 요기에서 현재 focusing된 view의 영역을 잡아준다.

            val (nowX, nowY) = ev.x.toInt() to ev.y.toInt()
            if (!area.contains(nowX, nowY)) {
                theme_name.clearFocus()
                theme_name.setBackgroundResource(R.drawable.item_border_round_black05)
                isFocusingEditText = false
            }
        }
        isFocusingEditText = false
        return super.dispatchTouchEvent(ev)
    }

    fun openColorSheetAndSetThemeColor() {
        val themeColorSelectBottomSheet = ThemeColorSelectBottomSheetFragment()
        themeColorSelectBottomSheet.setColorSavedListener(object: ThemeColorSelectBottomSheetFragment.ColorSavedListener {
            override fun onSaved(colorCode: Int) {
                colorSelected = colorCode
                theme_tag.setColorFilter(colorCode)

                val list = ColorList.colorList.filter { it.first == colorCode }
                val colorText = list[0].second
                theme_color_text.text = colorText
            }
        })

        val bundle = Bundle()
        bundle.putInt("COLOR_SELECTED", colorSelected)
        themeColorSelectBottomSheet.arguments = bundle  // bundle값으로 현재 지정된 색을 넘겨줌 BottomSheet에 넘겨줌
        themeColorSelectBottomSheet.show(supportFragmentManager, themeColorSelectBottomSheet.tag)
    }

    fun verifyThemeName(name: String?): Boolean =
        !name.isNullOrEmpty() && name.length <= 20
}
package com.fund.likeeat.ui

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivityAddReviewBinding
import com.fund.likeeat.databinding.ActivityAddThemeBinding
import com.fund.likeeat.databinding.ActivityChangeThemeNameBinding
import com.fund.likeeat.utilities.ColorList
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.OneThemeViewModel
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_add_theme.*
import kotlinx.android.synthetic.main.activity_change_theme_name.*
import kotlinx.android.synthetic.main.item_action_add_theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.lang.Exception

class UpdateThemeActivity : AppCompatActivity() {
    private var themeId: Long? = null
    var isFocusingEditText = false
    var isPublic: Boolean? = null

    private val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }
    private lateinit var binding: ActivityAddThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeId = intent.getLongExtra("THEME_ID", UID_DETACHED)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_theme)
        binding.lifecycleOwner = this

        var colorSelected: Int = Color.BLACK
        theme_color.setOnClickListener {
            ColorSheet().colorPicker(
                colors = ColorList.colorList,
                listener = { color: Int ->
                    colorSelected = color
                    theme_color.circleColor = color
                }).show(supportFragmentManager)
        }

        add_theme_title.text = "테마 수정"
        themeViewModel.theme.observe(this) { theme ->
            theme_add_name.setText(theme.name)
            theme_color.circleColor = theme.color
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

        action_enroll.setOnClickListener {
            themeViewModel.updateTheme(
                themeId!!,
                name = theme_add_name.text.toString(),
                color = colorSelected,
                isPublic = isPublic ?: throw Exception()
            )
            finish()
        }

        theme_add_name.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                theme_add_name.setBackgroundResource(R.drawable.item_border_round_primary100)
                isFocusingEditText = true
            }
        }

        theme_add_name.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(result: CharSequence?, start: Int, before: Int, count: Int) {
                if(result.isNullOrEmpty() || result.toString().length > 20) {
                    action_enroll.setBackgroundResource(R.drawable.item_fill_black05_round)
                } else {
                    action_enroll.setBackgroundResource(R.drawable.item_fill_black01_round)
                }
            }
        })

        layout_theme_set_public.setOnClickListener {
            if(isPublic != null) {
                if(isPublic!!) {
                    theme_is_public.text = "비공개"
                    image_public.setImageResource(R.drawable.ic_eye_off_24)
                } else {
                    theme_is_public.text = "공개"
                    image_public.setImageResource(R.drawable.ic_eye_24)
                }
                isPublic = !isPublic!!
            }
        }
    }

    // TODO 테마 이름을 입력하는 중, 테마 색상 다이얼로그에 바로 접근하면 Focusing이 해제 되지 않는 에러 (+ border 색도 변하지 않음)
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if(focusView != null && ev != null && isFocusingEditText) {
            val area = Rect()
            focusView.getGlobalVisibleRect(area)    // 요기에서 현재 focusing된 view의 영역을 잡아준다.

            val (nowX, nowY) = ev.x.toInt() to ev.y.toInt()
            if(!area.contains(nowX, nowY)) {
                theme_add_name.clearFocus()
                theme_add_name.setBackgroundResource(R.drawable.item_border_round_black05)
                isFocusingEditText = false
            }
        }
        isFocusingEditText = false
        return super.dispatchTouchEvent(ev)
    }
}
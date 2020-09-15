package com.fund.likeeat.ui

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRequest
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.ColorList
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_add_theme.*
import kotlinx.android.synthetic.main.activity_add_theme.action_add_new_theme
import kotlinx.android.synthetic.main.activity_set_theme.*

class AddThemeActivity : AppCompatActivity() {
    var isPublic = true
    var isFocusingEditText = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_theme)

        var colorSelected: Int = Color.BLACK
        theme_color.setOnClickListener {
            ColorSheet().colorPicker(
                colors = ColorList.colorList,
                listener = { color: Int ->
                    colorSelected = color
                    theme_color.circleColor = color
                }).show(supportFragmentManager)
        }

        action_add_new_theme.setOnClickListener {
            if (!theme_add_name.text.isNullOrEmpty() && theme_add_name.text.toString().length <= 20) {
                val theme = ThemeRequest(
                    MyApplication.pref.uid,
                    theme_add_name.text.toString(),
                    colorSelected,
                    isPublic
                )
                RetrofitProcedure.sendThemeToServer(theme)
                finish()
            }
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
                    action_add_new_theme.setImageResource(R.drawable.button_enroll_not_activated)
                } else {
                    action_add_new_theme.setImageResource(R.drawable.button_enroll_activated)
                }
            }
        })

        layout_theme_set_public.setOnClickListener {
            if(isPublic) {
                theme_is_public.text = "비공개"
                image_public.setImageResource(R.drawable.ic_eye_off_24)
            } else {
                theme_is_public.text = "공개"
                image_public.setImageResource(R.drawable.ic_eye_24)
            }
            isPublic = !isPublic
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
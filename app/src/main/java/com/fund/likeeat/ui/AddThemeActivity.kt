package com.fund.likeeat.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.ColorList
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_add_theme.*

class AddThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_theme)

        var colorSelected: Int = Color.BLACK
        theme_color_picker.setOnClickListener {
            ColorSheet().colorPicker(
                colors = ColorList.colorList,
                listener = { color: Int ->
                    colorSelected = color
                    theme_icon.circleColor = color
                }).show(supportFragmentManager)
        }

        add_theme_submit.setOnClickListener {
            val theme = Theme(
                MyApplication.pref.uid,
                theme_add_name.text.toString(),
                colorSelected,
                theme_add_public.isChecked
            )
            RetrofitProcedure.sendThemeToServer(theme)
            finish()
        }

        theme_add_name.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(result: CharSequence?, start: Int, before: Int, count: Int) {
                if(result.isNullOrEmpty()) theme_name.text = "내가 저장한 맛집 (기본)"
                else theme_name.text = result
            }
        })

        theme_add_public.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) theme_public.visibility = View.GONE
            else theme_public.visibility = View.VISIBLE
        }
    }
}
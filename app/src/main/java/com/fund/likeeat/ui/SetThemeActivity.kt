package com.fund.likeeat.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivitySetThemeBinding
import com.fund.likeeat.manager.KeyboardManager
import com.fund.likeeat.utilities.COLOR_NOT_SELECTED
import com.fund.likeeat.utilities.ColorList
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.fund.likeeat.widget.ThemeColorSelectBottomSheetFragment
import com.fund.likeeat.widget.ThemePublicSelectBottomSheetFragment
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_set_theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class SetThemeActivity : AppCompatActivity() {
    var themeId: Long? = null
    var isPublic = true
    var colorSelected: Int? = null

    var isFocusingEditText = false
    var isCorrectInputInformation = false

    var naverMapInfo: LatLng? = null

    val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }
    lateinit var binding: ActivitySetThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeId = intent.getLongExtra("THEME_ID", UID_DETACHED)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_theme)
        binding.apply {
            lifecycleOwner = this@SetThemeActivity

            layoutTag.setOnClickListener { openColorBottomSheetAndSetThemeColor() }
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
                    verifyCorrectInputInformationAndChangeButtonStyle(result.toString())
                }
            })

            layoutThemeSetPublic.setOnClickListener { openPublicBottomSheetAndSetPublicState() }

            /*layoutAddReviewInTheme.setOnClickListener {
                val intent = Intent(this@SetThemeActivity, SearchPlaceInThemeActivity::class.java)
                intent.putExtra("THEME_ID", NOT_CREATED)
                intent.putExtra(INTENT_KEY_LOCATION, naverMapInfo)
                startActivity(intent)
            }*/

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
        KeyboardManager.hideKeyboard(this, theme_name)
        return super.dispatchTouchEvent(ev)
    }

    private fun openColorBottomSheetAndSetThemeColor() {
        val themeColorSelectBottomSheet = ThemeColorSelectBottomSheetFragment()
        themeColorSelectBottomSheet.setColorSavedListener(object: ThemeColorSelectBottomSheetFragment.ColorSavedListener {
            override fun onSaved(colorCode: Int) {
                colorSelected = colorCode
                theme_tag.setColorFilter(colorCode)

                verifyCorrectInputInformationAndChangeButtonStyle(binding.themeName.text.toString())

                val list = ColorList.colorList.filter { it.first == colorCode }
                val colorText = list[0].second
                theme_color_text.text = colorText
            }
        })

        val bundle = Bundle()
        bundle.putInt("COLOR_SELECTED", colorSelected ?: COLOR_NOT_SELECTED)
        themeColorSelectBottomSheet.arguments = bundle  // bundle값으로 현재 지정된 색을 넘겨줌 BottomSheet에 넘겨줌
        themeColorSelectBottomSheet.show(supportFragmentManager, themeColorSelectBottomSheet.tag)
    }

    private fun openPublicBottomSheetAndSetPublicState() {
        ThemePublicSelectBottomSheetFragment().apply {
            setPublicSavedListener(object: ThemePublicSelectBottomSheetFragment.PublicSavedListener {
                override fun onSaved(isPublic: Boolean) {
                    this@SetThemeActivity.isPublic = isPublic
                    if (isPublic) {
                        binding.themeIsPublic.text = resources.getString(R.string.theme_public_open)
                        binding.imagePublic.setImageResource(R.drawable.ic_baseline_visibility_24)
                    } else {
                        binding.themeIsPublic.text = resources.getString(R.string.theme_public_close)
                        binding.imagePublic.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                    }
                }
            })

            val bundle = Bundle()
            bundle.putBoolean("PUBLIC_SELECTED", isPublic)
            arguments = bundle
            show(supportFragmentManager, tag)
        }
    }

    fun verifyCorrectInputInformationAndChangeButtonStyle(name: String?) {
        if(verifyThemeName(name) && verifyThemeColor()) {
            isCorrectInputInformation = true
            action_enroll.setBackgroundResource(R.drawable.item_fill_black01_round)
        } else {
            isCorrectInputInformation = false
            action_enroll.setBackgroundResource(R.drawable.item_fill_black05_round)
        }
    }

    private fun verifyThemeName(name: String?): Boolean =
        !name.isNullOrEmpty() && name.length <= 20

    private fun verifyThemeColor() = colorSelected != null
}
package com.fund.likeeat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.fund.likeeat.R
import com.fund.likeeat.adapter.OnBoardingAdapter
import com.fund.likeeat.adapter.OnStartButtonClickListener
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.widget.OnBoardingItem
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.android.synthetic.main.item_container_onboarding.*

class OnBoardingActivity : AppCompatActivity() {
    private var onBoardingAdapter: OnBoardingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        setUpOnBoardingItems()
        view_pager.adapter = onBoardingAdapter

        setUpOnBoardingIndicators()
        setCurrentOnBoardingIndicator(0)

        view_pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnBoardingIndicator(position)
            }
        })
    }

    private fun setUpOnBoardingItems() {
        val onBoardingItems = mutableListOf<OnBoardingItem>().apply {
            add(
                OnBoardingItem(
                    R.drawable.image_on_boarding_first,
                    "간편한 맛집등록",
                    "라이크잇에서 간편하게\n맛집을 등록해보세요"
                )
            )

            add(
                OnBoardingItem(
                    R.drawable.image_on_boarding_second,
                    "나만의 테마분류",
                    "내가 지정한 테마로\n맛집을 보기쉽게 분류하세요"
                )
            )

            add(
                OnBoardingItem(
                    R.drawable.image_on_boarding_third,
                    "믿을수 있는 친구의 맛집",
                    "라이크잇을 통해 믿을수 있는\n친구의 맛집목록을 확인하세요"
                )
            )
        }

        onBoardingAdapter = OnBoardingAdapter(onBoardingItems)
        onBoardingAdapter?.setOnStartButtonClickListener(object: OnStartButtonClickListener {
            override fun onClick() {
                goToLoginActivity(view_pager.currentItem)
            }
        })
    }

    private fun setUpOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(3)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(24, 0, 24, 0) }

        for(i in 0..2) {
            indicators[i] = ImageView(this)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(
                this,
                R.drawable.item_onboarding_indicator_inactive
            ))
            indicators[i]?.layoutParams = layoutParams
            indicator_layout.addView(indicators[i])
        }

    }

    private fun setCurrentOnBoardingIndicator(index: Int) {
        for(i in 0..2) {
            val imageView = indicator_layout.getChildAt(i) as ImageView
            if(i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.item_onboarding_indicator_active
                ))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    this,
                    R.drawable.item_onboarding_indicator_inactive
                ))
            }
        }
    }

    private fun goToLoginActivity(position: Int) {
        if (position == 2) {
            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)

            MyApplication.pref.isNeedToSeeOnBoarding = false
            finish()
        }
    }
}
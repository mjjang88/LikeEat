package com.fund.likeeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.CardLongClickListener
import com.fund.likeeat.adapter.ReviewsInThemeAdapter
import com.fund.likeeat.databinding.ActivityReviewsInThemeBinding
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.fund.likeeat.widget.SetReviewInThemeBottomSheet
import kotlinx.android.synthetic.main.activity_reviews_in_theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReviewsInThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewsInThemeBinding
    val oneThemeViewModel: OneThemeViewModel by viewModel { parametersOf(intent.getLongExtra("THEME_ID", -12)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reviews_in_theme)
        binding.apply {
            lifecycleOwner = this@ReviewsInThemeActivity
            themeViewModel = oneThemeViewModel
            actionBack.setOnClickListener { finish() }
        }

        val adapter = ReviewsInThemeAdapter().apply {
            setOnCardLongClickListener(object: CardLongClickListener {
                override fun onLongClick(reviewId: Long) {
                    val bundle = Bundle().apply {
                        putLong("REVIEW_ID", reviewId)
                        putLong("THEME_ID", intent.getLongExtra("THEME_ID", -12))
                    }

                    val dialog =
                        SetReviewInThemeBottomSheet()
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, dialog.tag)
                }

            })
        }
        recycler.adapter = adapter

        oneThemeViewModel.reviewIdList.observe(this) { result ->
            val reviewIdList = result.map { it.reviewId }

            GlobalScope.launch { oneThemeViewModel.getReviews(reviewIdList) }
        }

        oneThemeViewModel.reviewOneTheme.observe(this) { result ->
            adapter.submitList(result)
        }

    }
}
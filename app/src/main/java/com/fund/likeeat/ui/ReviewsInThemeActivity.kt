package com.fund.likeeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsInThemeAdapter
import com.fund.likeeat.databinding.ActivityReviewsInThemeBinding
import com.fund.likeeat.viewmodels.OneThemeViewModel
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
        }

        val adapter = ReviewsInThemeAdapter()
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
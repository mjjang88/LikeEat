package com.fund.likeeat.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsAdapter
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityReviewsBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.INTENT_KEY_THEME
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.MapOneThemeViewModel
import com.fund.likeeat.viewmodels.ReviewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ReviewsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReviewsBinding
    private val oneThemeViewModel: MapOneThemeViewModel by inject()
    private val reviewsViewModel: ReviewsViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityReviewsBinding>(
            this,
            R.layout.activity_reviews
        )
        binding.lifecycleOwner = this

        Log.i("UID_ATTACHED", intent.getLongExtra("uid", UID_DETACHED).toString())

        val adapter = ReviewsAdapter()
        binding.recyclerViewReviewList.adapter = adapter

        val theme = intent.getParcelableExtra<Theme>(INTENT_KEY_THEME)
        theme?.let {
            binding.textTitle.text = it.name

            oneThemeViewModel.getReviewIdListByThemeId(it.id)
            oneThemeViewModel.reviewIdList.observe(this) {
                val reviewIdList = it.map { it.reviewId }
                lifecycleScope.launch(Dispatchers.IO) { oneThemeViewModel.getReviews(reviewIdList) }
            }
            oneThemeViewModel.reviewOneTheme.observe(this) {
                reviewsViewModel.getReviewFullList(it)
            }
            reviewsViewModel.reviewFull.observe(this) {
                adapter.submitList(it)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
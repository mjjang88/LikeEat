package com.fund.likeeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ActivityViewReviewBinding
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW

class ViewReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityViewReviewBinding>(
            this,
            R.layout.activity_view_review
        )

        initReview(binding)
    }

    private fun initReview(binding: ActivityViewReviewBinding) {
        val review = intent.getParcelableExtra<Review>(INTENT_KEY_REVIEW)

        binding.review = review
    }
}
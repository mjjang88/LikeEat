package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ReviewRepository

class OneReviewViewModel(
    reviewRepository: ReviewRepository,
    reviewId: Long
): ViewModel() {
    val review = reviewRepository.getReviewById(reviewId)
}

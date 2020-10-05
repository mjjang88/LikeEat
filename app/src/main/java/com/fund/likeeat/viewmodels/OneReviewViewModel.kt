package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository

class OneReviewViewModel(
    val reviewRepository: ReviewRepository,
    reviewId: Long
): ViewModel() {
    val review = reviewRepository.getReviewById(reviewId)
    val reviewForDelete: MutableLiveData<Review> = MutableLiveData()

    fun getBaseReview(reviewId: Long) {
        reviewForDelete.postValue(reviewRepository.getReviewById(reviewId).value)
    }
}

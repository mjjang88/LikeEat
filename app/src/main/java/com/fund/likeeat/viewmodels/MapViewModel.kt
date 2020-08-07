package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository

class MapViewModel internal constructor(
    reviewRepository: ReviewRepository
) : ViewModel(){
    val review: LiveData<List<Review>> = reviewRepository.getReviewList()
}
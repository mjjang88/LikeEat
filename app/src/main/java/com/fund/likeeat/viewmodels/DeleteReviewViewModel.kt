package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.manager.MyApplication

class DeleteReviewViewModel(
    val reviewRepository: ReviewRepository
) :ViewModel() {
    val reviewsList: MutableLiveData<List<Review>> = MutableLiveData()

    fun getAllReviews(x: Double, y: Double, name: String) {
        reviewsList.postValue(reviewRepository.getReviewListByPlace(MyApplication.pref.uid, x, y, name))
    }
}
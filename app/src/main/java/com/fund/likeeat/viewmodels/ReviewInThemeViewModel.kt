package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.manager.MyApplication

class ReviewInThemeViewModel(
    val reviewRepository: ReviewRepository
): ViewModel() {

    val allReviewsList: MutableLiveData<List<Review>> = MutableLiveData()

    fun getAllReviews(x: Double, y: Double, name: String) {
        allReviewsList.postValue(reviewRepository.getReviewListByPlace(MyApplication.pref.uid, x, y, name))
    }
}
package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MapPreviewViewModel internal constructor(
    val reviewDao: ReviewDao
): ViewModel(){
    suspend fun getReviewBySamePlace(place: Place?): Review? {
        val review = viewModelScope.async(Dispatchers.IO) {
            val reviewList = reviewDao.getReviewListBySamePlace2(place?.name?: "", place?.address?: "")
            if (reviewList.isNotEmpty()) {
                reviewList[0]
            } else {
                null
            }
        }

        return review.await()
    }
}
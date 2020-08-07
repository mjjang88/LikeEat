package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ReviewRepository (
    private val reviewDao: ReviewDao
){
    fun getReviewList(): LiveData<List<Review>> {
        return reviewDao.getReviewList()
    }
}
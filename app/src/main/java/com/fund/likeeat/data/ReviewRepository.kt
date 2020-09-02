package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ReviewRepository (
    private val reviewDao: ReviewDao
){
    fun getReviewList(): LiveData<List<Review>> {
        return reviewDao.getReviewList()
    }

    fun getReviewList2(): List<Review> {
        return reviewDao.getReviewList2()
    }
}
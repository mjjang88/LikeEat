package com.fund.likeeat.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReviewRepository (
    private val reviewDao: ReviewDao
){
    fun getReviewList(): LiveData<List<Review>> {
        return reviewDao.getReviewList()
    }

    fun getReviewList2(): List<Review> {
        return reviewDao.getReviewList2()
    }

    fun getReviewByTheme(reviewId: List<Long>): List<Review> {
        return reviewDao.getReviewByTheme(reviewId)
    }

    fun getReviewById(reviewId: Long): LiveData<Review> {
        return reviewDao.getReviewById(reviewId)
    }

    fun getReviewByUidAndWord(uid: Long, word: String): MutableList<Review> {
        Log.i("WORD-REPOSITORY", word.toString())
        return reviewDao.getReviewByUidAndWord(uid, word)
    }
}
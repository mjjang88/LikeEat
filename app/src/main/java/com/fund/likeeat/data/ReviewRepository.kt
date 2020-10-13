package com.fund.likeeat.data

import android.util.Log
import androidx.lifecycle.LiveData

class ReviewRepository (
    private val reviewDao: ReviewDao
){
    fun getReviewList(): LiveData<List<Review>> {
        return reviewDao.getReviewList()
    }

    fun getReviewListByUid(uid: Long): LiveData<List<Review>> {
        return reviewDao.getReviewListByUid(uid)
    }

    fun getReviewList2(): List<Review> {
        return reviewDao.getReviewList2()
    }

    fun getReviewByTheme(reviewId: List<Long>): List<Review> {
        return reviewDao.getReviewByTheme(reviewId)
    }

    fun getReviewByReviewIdWithNoSameData(reviewId: List<Long>): List<Review> {
        return reviewDao.getReviewByReviewIdWithNoSameData(reviewId)
    }

    fun getReviewById(reviewId: Long): LiveData<Review> {
        return reviewDao.getReviewById(reviewId)
    }

    fun getReviewByUidAndWord(uid: Long, word: String): MutableList<Review> {
        Log.i("WORD-REPOSITORY", word.toString())
        return reviewDao.getReviewByUidAndWord(uid, word)
    }

    fun getReviewListByPlace(uid: Long, x: Double, y: Double, placeName: String): List<Review> {
        return reviewDao.getReviewListByPlace(uid, x, y, placeName)
    }

    fun getPlaceCount(uid: Long): LiveData<List<Review>> {
        return reviewDao.getPlaceCount(uid)
    }
}
package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class ReviewRepository (
    private val placeDao: ReviewDao
){
    fun getPlaceList(): LiveData<List<Review>> {
        return placeDao.getPlaceList()
    }
}
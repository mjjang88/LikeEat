package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class PlaceRepository (
    private val placeDao: PlaceDao
){
    fun getPlaceList(): LiveData<List<Place>> {
        return placeDao.getPlaceList()
    }
}
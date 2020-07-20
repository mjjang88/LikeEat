package com.fund.likeeat.data

import androidx.lifecycle.LiveData

class PlaceRepository private constructor(
    private val placeDao: PlaceDao
){
    fun getPlaceList(): LiveData<List<Place>> {
        return placeDao.getPlaceList()
    }

    companion object {
        @Volatile private var instance: PlaceRepository? = null

        fun getInstance(placeDao: PlaceDao) =
            instance ?: synchronized(this) {
                instance ?: PlaceRepository(placeDao).also { instance = it }
            }
    }
}
package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceRepository

class MapViewModel internal constructor(
    placeRepository: PlaceRepository
) : ViewModel(){
    val place: LiveData<List<Place>> = placeRepository.getPlaceList()
}
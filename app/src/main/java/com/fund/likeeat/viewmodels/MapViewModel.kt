package com.fund.likeeat.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceRepository
import kotlinx.coroutines.launch

class MapViewModel internal constructor(
    placeRepository: PlaceRepository
) : ViewModel(){
    val place: LiveData<List<Place>> = placeRepository.getPlaceList()
}
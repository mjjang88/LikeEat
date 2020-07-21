package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fund.likeeat.data.PlaceRepository

class MapViewModelFactory(
    private val placeRepository: PlaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(placeRepository) as T
    }
}
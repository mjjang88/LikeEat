package com.fund.likeeat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fund.likeeat.data.PlaceRepository

class ReviewsViewModelFactory(
    private val placeRepository: PlaceRepository,
    private val uid: Long
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewsViewModel(placeRepository, uid) as T
    }

}
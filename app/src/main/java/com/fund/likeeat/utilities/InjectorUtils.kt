package com.fund.likeeat.utilities

import android.content.Context
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.PlaceRepository
import com.fund.likeeat.viewmodels.ReviewsViewModelFactory

object InjectorUtils {
    private fun getPlaceRepository(context: Context): PlaceRepository {
        return PlaceRepository.getInstance(AppDatabase.getInstance(context).placeDao())
    }

    fun provideReviewsViewModelFactory(context: Context, uid: Long): ReviewsViewModelFactory {
        return ReviewsViewModelFactory(getPlaceRepository(context), uid)
    }
}
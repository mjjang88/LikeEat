package com.fund.likeeat.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.PlaceRepository
import com.fund.likeeat.viewmodels.MapViewModelFactory

object InjectorUtils {

    private fun getPlaceRepository(context: Context): PlaceRepository {
        return PlaceRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).placeDao()
        )
    }

    fun provideMapViewModelFactory(fragment: Fragment): MapViewModelFactory {
        val placeRepository = getPlaceRepository(fragment.requireContext())
        return MapViewModelFactory(placeRepository)
    }
}
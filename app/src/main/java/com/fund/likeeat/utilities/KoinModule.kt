package com.fund.likeeat.utilities

import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.data.ThemeRepository
import com.fund.likeeat.viewmodels.MapViewModel
import com.fund.likeeat.viewmodels.ReviewsViewModel
import com.fund.likeeat.viewmodels.SearchPlaceViewModel
import com.fund.likeeat.viewmodels.ThemeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /**
     * Database Module
     */
    single { AppDatabase.getInstance(androidApplication()) }
    single(createdAtStart = false) { get<AppDatabase>().reviewDao() }
    single(createdAtStart = false) { get<AppDatabase>().themeDao() }
    single { ReviewRepository(get()) }
    single { ThemeRepository(get()) }

    /**
     * ViewModel Module
     */
    viewModel { MapViewModel(get()) }
    viewModel { (uid: Long) -> ReviewsViewModel(get(), uid) }
    viewModel { (uid: Long) -> ThemeViewModel(get(), uid) }
    viewModel { SearchPlaceViewModel() }
}
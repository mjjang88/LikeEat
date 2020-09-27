package com.fund.likeeat.utilities

import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.data.ThemeRepository
import com.fund.likeeat.ui.SearchPlaceInThemeActivity
import com.fund.likeeat.viewmodels.*
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
    single(createdAtStart = false) { get<AppDatabase>().reviewThemeLinkDao() }
    single(createdAtStart = false) { get<AppDatabase>().kakaoFriendDao() }
    single(createdAtStart = false) { get<AppDatabase>().friendLinkDao() }
    single { ReviewRepository(get()) }
    single { ThemeRepository(get()) }

    /**
     * ViewModel Module
     */
    viewModel { (uid: Long) -> MapViewModel(get(), get(), get(), uid) }
    viewModel { (uid: Long) -> ReviewsViewModel(get(), get(), get(), uid) }
    viewModel { (uid: Long) -> AllThemesViewModel(get(), uid) }
    viewModel { SearchPlaceViewModel() }
    viewModel { (uid: Long) -> AddReviewViewModel(get(), get(), uid) }
    viewModel { (id: Long) -> ReviewDetailViewModel(get(), get(), get(), id) }
    viewModel { MapPreviewViewModel(get()) }
    viewModel { (id: Long) -> OneThemeViewModel(get(), get(), get(), id) }
    viewModel { (reviewId: Long, themeId: Long) -> ReviewThemeLinkViewModel(get(), reviewId, themeId) }
    viewModel { (reviewId: Long) -> OneReviewViewModel(get(), reviewId) }
    viewModel { SearchPlaceInThemeViewModel(get()) }
    viewModel { (uid: Long) -> AddFriendViewModel(get(), get(), uid) }
    viewModel { (uid: Long) -> FriendViewModel(get(), get(), uid) }
}
package com.fund.likeeat.viewmodels

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.*
import com.fund.likeeat.network.RetrofitProcedure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class OneThemeViewModel(
    themeRepository: ThemeRepository,
    val reviewRepository: ReviewRepository,
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    themeId: Long
): ViewModel() {
    val theme = themeRepository.getTheme(themeId)

    var reviewIdList: LiveData<List<ReviewThemeLink>> = reviewThemeLinkDao.getListByThemeId(themeId)
    val reviewOneTheme: MutableLiveData<List<Review>> = MutableLiveData()
    val reviewOneThemeNoSameData: MutableLiveData<List<Review>> = MutableLiveData()

    fun updateTheme(
        activity: Activity,
        id: Long,
        name: String = theme.value!!.name,
        color: Int = theme.value!!.color,
        isPublic: Boolean = theme.value!!.isPublic
    ) {
        RetrofitProcedure.updateThemeById(
            id,
            ThemeChanged(name, color, isPublic)
        )
    }

    fun getReviews(reviewIdList: List<Long>) {
        reviewOneTheme.postValue(reviewRepository.getReviewByTheme(reviewIdList))
    }

    fun getReviewsNoSameData(reviewIdList: List<Long>) {
        reviewOneThemeNoSameData.postValue(reviewRepository.getReviewByReviewIdWithNoSameData(reviewIdList))
    }
}

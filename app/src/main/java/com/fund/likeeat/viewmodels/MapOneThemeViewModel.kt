package com.fund.likeeat.viewmodels

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.*
import com.fund.likeeat.network.RetrofitProcedure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MapOneThemeViewModel(
    val themeRepository: ThemeRepository,
    val reviewRepository: ReviewRepository,
    val reviewThemeLinkDao: ReviewThemeLinkDao
): ViewModel() {
    val theme: MutableLiveData<Theme> = MutableLiveData()
    var reviewIdList: MutableLiveData<List<ReviewThemeLink>> = MutableLiveData()
    val reviewOneTheme: MutableLiveData<List<Review>> = MutableLiveData()

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
        GlobalScope.launch { reviewOneTheme.postValue(reviewRepository.getReviewByTheme(reviewIdList)) }
    }

    fun getThemeByThemeId(themeId: Long) {
        theme.postValue(themeRepository.getThemeByThemeId(themeId))
    }

    fun getReviewIdListByThemeId(themeId: Long) {
        GlobalScope.launch { reviewIdList.postValue(reviewThemeLinkDao.getReviewListByThemeId(themeId)) }
    }
}

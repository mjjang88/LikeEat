package com.fund.likeeat.viewmodels

import androidx.lifecycle.*
import com.fund.likeeat.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewDetailViewModel internal constructor(
    val reviewDao: ReviewDao,
    val themeDao: ThemeDao,
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    val uid: Long
): ViewModel() {

    val inputReview: MutableLiveData<Review?> = MutableLiveData()
    val reviews: LiveData<List<Review>> = Transformations.switchMap(inputReview) {
        reviewDao.getReviewListBySamePlace(inputReview.value?.place_name?: "", inputReview.value?.address_name?: "")
    }

    val themeList: MutableLiveData<List<Theme>> = MutableLiveData()

    fun getThemeList(review: Review) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val themeListAll = themeDao.getThemeList2(uid)
                val reviewThemeLinkList = reviewThemeLinkDao.getList()

                val themeListResult = ArrayList<Theme>()
                reviewThemeLinkList.forEach { reviewThemeLink: ReviewThemeLink ->
                    if (review.id == reviewThemeLink.reviewId) {
                        themeListAll.find {theme ->
                            theme.id == reviewThemeLink.themeId
                        }?.let {
                            themeListResult.add(it)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    themeList.value = themeListResult
                }

            } catch (e: Throwable) {
                e.stackTrace
            }
        }
    }
}
package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel internal constructor(
    val reviewRepository: ReviewRepository,
    val themeDao: ThemeDao,
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    val uid: Long
) : ViewModel(){
    val review: LiveData<List<Review>> = reviewRepository.getReviewList()
    val theme: LiveData<List<Theme>> = themeDao.getThemeList(uid)

    val reviewFull = MutableLiveData<ArrayList<ReviewFull>>().apply { value = arrayListOf() }

    fun getReviewFullList(reviewList: List<Review>) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val themeList = themeDao.getThemeList2(uid)
                val reviewThemeLinkList = reviewThemeLinkDao.getList()

                val reviewFullList = ArrayList<ReviewFull>()

                reviewList.forEach {review: Review ->
                    val themeListForAdd = ArrayList<Theme>()

                    reviewThemeLinkList.forEach { reviewThemeLink: ReviewThemeLink ->
                        if (review.id == reviewThemeLink.reviewId) {
                            themeList.find {theme ->
                                theme.id == reviewThemeLink.themeId
                            }?.let {
                                themeListForAdd.add(it)
                            }

                        }
                    }

                    val reviewFull = ReviewFull(review.id, review, themeListForAdd)
                    reviewFullList.add(reviewFull)
                }

                withContext(Dispatchers.Main) {
                    reviewFull.value = reviewFullList
                }

            } catch (e: Throwable) {
                e.stackTrace
            }
        }
    }
}
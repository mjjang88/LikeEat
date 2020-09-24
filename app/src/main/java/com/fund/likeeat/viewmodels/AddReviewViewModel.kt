package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.*
import com.fund.likeeat.network.ReviewServerWrite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddReviewViewModel internal constructor(
    val themeRepository: ThemeRepository,
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    val uid: Long
): ViewModel() {

    val editedReview: MutableLiveData<ReviewServerWrite?> = MutableLiveData(ReviewServerWrite(true,null,null,null,null,null,null,null,null,-1,null,null))

    fun getThemeList(): List<Theme> {
        return themeRepository.getThemeList2(uid)
    }

    fun setCategory(category: String) {
        editedReview.value?.category = category
        editedReview.value = editedReview.value
    }

    fun setTheme(themeIds: String) {
        editedReview.value?.themeIds = themeIds
        editedReview.value = editedReview.value
    }

    suspend fun getThemeIds(review: Review): String {

        val result = viewModelScope.async(Dispatchers.IO) {

            var strResult: String = ""

            try {
                val themeListAll = getThemeList()
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

                themeListResult.forEach {
                    if (!strResult.isNullOrBlank()) {
                        strResult += ","
                    }
                    strResult += it.id
                }

                strResult

            } catch (e: Throwable) {
                e.stackTrace
                ""
            }
        }

        return result.await()
    }

    fun setVisitDate(date: String) {
        editedReview.value?.visitedDayYmd = date
        editedReview.value = editedReview.value
    }

    fun setEvaluation(eval: String) {
        editedReview.value?.serviceQuality = eval
        editedReview.value = editedReview.value
    }

    fun setCompanion(companion: String) {
        editedReview.value?.companions = companion
        editedReview.value = editedReview.value
    }

    fun setPrice(price: String) {
        editedReview.value?.priceRange = price
        editedReview.value = editedReview.value
    }

    fun setToilet(toilet: String) {
        editedReview.value?.toliets = toilet
        editedReview.value = editedReview.value
    }

    fun setRevisit(revisit: String) {
        editedReview.value?.revisit = revisit
        editedReview.value = editedReview.value
    }
}
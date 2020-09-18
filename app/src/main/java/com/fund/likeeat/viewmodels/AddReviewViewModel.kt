package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRepository
import com.fund.likeeat.network.ReviewServerWrite

class AddReviewViewModel internal constructor(
    val themeRepository: ThemeRepository,
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
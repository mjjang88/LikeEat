package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.data.ReviewThemeLinkDao

class ReviewThemeLinkViewModel(
    reviewThemeLinkDao: ReviewThemeLinkDao,
    reviewId: Long,
    themeId: Long
): ViewModel() {
    val reviewIdList: LiveData<List<ReviewThemeLink>> = reviewThemeLinkDao.getListByThemeId(themeId)
    val themeIdList: LiveData<List<ReviewThemeLink>> = reviewThemeLinkDao.getListByReviewId(reviewId)

    // "1,3,5" 식으로 나타내게 됨
    fun getThemeIdString(): String? {
        val builder = StringBuilder()
        val list = themeIdList.value
        list?.let {
            for((index, link) in it.withIndex()) {
                builder.append(link.themeId)
                if(index != it.size-1) {
                    builder.append(",")
                }
            }
        }?:return null

        return builder.toString()
    }
}

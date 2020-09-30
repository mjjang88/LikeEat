package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.data.ReviewThemeLinkDao

class ReviewThemeLinkViewModel(
    val reviewThemeLinkDao: ReviewThemeLinkDao
): ViewModel() {
    val reviewIdList: MutableLiveData<List<ReviewThemeLink>> = MutableLiveData()
    val themeIdList: MutableLiveData<List<ReviewThemeLink>> = MutableLiveData()

    fun getThemeIdList(reviewId: Long) {
        themeIdList.postValue(reviewThemeLinkDao.getListByReviewId(reviewId))
    }

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

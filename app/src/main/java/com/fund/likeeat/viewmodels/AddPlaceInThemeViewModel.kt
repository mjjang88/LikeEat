package com.fund.likeeat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.data.ReviewThemeLinkDao
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.NO_PLACE_NAME
import com.fund.likeeat.utilities.NO_X_VALUE
import com.fund.likeeat.utilities.NO_Y_VALUE

class AddPlaceInThemeViewModel(
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    val reviewRepository: ReviewRepository
) : ViewModel() {
    // 전체를 한번에 할지, 한번씩 전체를 할지
    //val reviewMap: MutableLiveData<HashMap<Long, List<Review>>> = MutableLiveData()
    val reviewList: MutableLiveData<List<Review>> = MutableLiveData()
    val linkedRelationList: MutableLiveData<List<ReviewThemeLink>> = MutableLiveData()

    /*fun getReviewListByXYName(review: Review) {
        reviewMap.value!![review.id] = reviewRepository.getReviewListByPlace(MyApplication.pref.uid, review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.place_name ?: NO_PLACE_NAME)
    }*/

    fun getReviewListByXYName(review: Review) {
        reviewList.postValue(reviewRepository.getReviewListByPlace(MyApplication.pref.uid, review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.place_name ?: NO_PLACE_NAME))
    }

    fun getRelations(review: Review) {
        linkedRelationList.postValue(reviewThemeLinkDao.getListByReviewId(review.id))
    }

    // "1,3,5" 식으로 나타내게 됨
    fun getThemeIdString(): String? {
        val builder = StringBuilder()
        val list = linkedRelationList.value
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
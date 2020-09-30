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
    val reviewPair: MutableLiveData<List<Pair<Review, Pair<List<ReviewThemeLink>, List<Review>>>>> = MutableLiveData()
    val tempReviewPair = mutableListOf<Pair<Review, Pair<List<ReviewThemeLink>, List<Review>>>>()


    fun insertReviewPair(review: Review) {
        val item = Pair(
            review, Pair(
                reviewThemeLinkDao.getListByReviewId(review.id),
                reviewRepository.getReviewListByPlace(MyApplication.pref.uid, review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.place_name ?: NO_PLACE_NAME)
            )
        )
        tempReviewPair.add(item)
        reviewPair.postValue(tempReviewPair)
    }

    // "1,3,5" 식으로 나타내게 됨
    fun getThemeIdString(reviewThemeLinkList: List<ReviewThemeLink>): String? {
        val builder = StringBuilder()
        reviewThemeLinkList.let {
            for((index, link) in it.withIndex()) {
                builder.append(link.themeId)
                if(index != it.size-1) {
                    builder.append(",")
                }
            }
        }
        return builder.toString()
    }
}
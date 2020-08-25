package com.fund.likeeat.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure

class ReviewsViewModel internal constructor(
    val reviewRepository: ReviewRepository,
    val uid: Long
): ViewModel() {
    var reviewList: MutableLiveData<List<Review>>? = MutableLiveData()

    val review: LiveData<List<Review>>?
        get() {
            when(uid) {
                MyApplication.pref.uid -> {
                    return reviewRepository.getReviewList()
                }
                else -> {
                    getReviewByUid(uid, reviewList)
                    return reviewList
                }
            }
        }

    private fun getReviewByUid(uid: Long, reviewList: MutableLiveData<List<Review>>?) {
        RetrofitProcedure.getUserReview(uid, reviewList)
        Log.i("UID_RETROFIT", uid.toString())
    }

}
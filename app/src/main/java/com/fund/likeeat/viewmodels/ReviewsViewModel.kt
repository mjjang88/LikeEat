package com.fund.likeeat.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceRepository
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure

/*
이전 fragment(map fragment)에서 사용자의 uid만 서버로 넘겨주면
이쪽 viewModel에서 retrofit을 사용하여 모든 맛집 데이터를 받아옴

따라서, 친구의 맛집 목록을 보는 경우에도
친구의 uid만 이전 프래그먼트에서 넘겨주기만 하면 된다.
 */

class ReviewsViewModel internal constructor(
    val placeRepository: PlaceRepository,
    val uid: Long
): ViewModel() {
    var reviewList: MutableLiveData<List<Place>>? = MutableLiveData()

    val review: LiveData<List<Place>>?
        get() {
            when(uid) {
                MyApplication.pref.uid -> {
                    return placeRepository.getPlaceList()
                }
                else -> {
                    getPlaceByUid(uid, reviewList)
                    return reviewList
                }
            }
        }

    private fun getPlaceByUid(uid: Long, reviewList: MutableLiveData<List<Place>>?) {
        RetrofitProcedure.getPlaceByUid(uid, reviewList)
        Log.i("UID_RETROFIT", uid.toString())
    }

}
package com.fund.likeeat.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewRepository
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.GpsTracker
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchPlaceInThemeViewModel(val reviewRepository: ReviewRepository) : ViewModel() {

    val searchWord: MutableLiveData<String?> = MutableLiveData("")
    val searchOption: MutableLiveData<Int> = MutableLiveData(0)
    val locate: MutableLiveData<LatLng> = MutableLiveData()
    val x: MutableLiveData<Double> = MutableLiveData()
    val y: MutableLiveData<Double> = MutableLiveData()

    var reviewList: MutableLiveData<MutableList<Review>> = MutableLiveData()

    fun searchFromWord(word: String) {
        GlobalScope.launch {
            reviewList.postValue(reviewRepository.getReviewByUidAndWord(MyApplication.pref.uid, "%${word}%"))
        }
    }


    fun search() {
        val word = searchWord.value
        Log.i("WORD", word.toString())
        val option = searchOption.value
        if (word.isNullOrBlank()) {
            return
        }

        when (option) {
            SEARCH_OPTION_NEAR_CURRUNT_LOCATION -> {
                val gpsTracker = GpsTracker(MyApplication.applicationContext())
                x.value = gpsTracker.getLongitude()
                y.value = gpsTracker.getLatitude()
            }
            SEARCH_OPTION_NEAR_MAP_LOCATION -> {
                x.value = locate.value?.longitude
                y.value = locate.value?.latitude
            }
        }
        searchFromWord(word)

    }


}
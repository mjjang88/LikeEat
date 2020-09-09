package com.fund.likeeat.viewmodels

import androidx.lifecycle.*
import com.fund.likeeat.data.Place
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.KaKaoRetrofit
import com.fund.likeeat.utilities.GpsTracker
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val SEARCH_OPTION_NEAR_CURRUNT_LOCATION = 0x000000
const val SEARCH_OPTION_NEAR_MAP_LOCATION = 0x000001
const val SEARCH_OPTION_ACCURACY = 0x000002

class SearchPlaceViewModel : ViewModel() {

    val searchWord: MutableLiveData<String?> = MutableLiveData("")
    val searchOption: MutableLiveData<Int> = MutableLiveData(0)
    val locate: MutableLiveData<LatLng> = MutableLiveData()
    val placeList = MutableLiveData<ArrayList<Place>>().apply { value = arrayListOf() }

    init {
        getPlaceList()
    }

    fun getPlaceList() {

        val word = searchWord.value
        val option = searchOption.value
        if (word.isNullOrBlank()) {
            return
        }

        var x : Double? = null
        var y : Double? = null
        val rad = 20000
        when (option) {
            SEARCH_OPTION_NEAR_CURRUNT_LOCATION -> {
                val gpsTracker = GpsTracker(MyApplication.applicationContext())
                x = gpsTracker.getLongitude()
                y = gpsTracker.getLatitude()
            }
            SEARCH_OPTION_NEAR_MAP_LOCATION -> {
                x = locate.value?.longitude
                y = locate.value?.latitude
            }
        }

        placeList.value?.clear()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                KaKaoRetrofit.getService().getPlace(word, x, y, rad).apply {
                    this.body()?.let {
                        placeList.value?.addAll(it.documents)
                    }

                    withContext(Dispatchers.Main) {
                        placeList.value = placeList.value
                    }
                }
            } catch (e: Throwable) {
                e.stackTrace
            }
        }
    }
}
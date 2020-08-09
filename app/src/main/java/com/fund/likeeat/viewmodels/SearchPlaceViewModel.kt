package com.fund.likeeat.viewmodels

import androidx.lifecycle.*
import com.fund.likeeat.data.Place
import com.fund.likeeat.network.KaKaoRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPlaceViewModel : ViewModel() {

    val searchWord: MutableLiveData<String?> = MutableLiveData("")
    val placeList = MutableLiveData<ArrayList<Place>>().apply { value = arrayListOf() }

    init {
        getPlaceList()
    }

    fun getPlaceList() {

        val word = searchWord.value
        if (word.isNullOrBlank()) {
            return
        }

        placeList.value?.clear()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                KaKaoRetrofit.getService().getPlace(word).apply {
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
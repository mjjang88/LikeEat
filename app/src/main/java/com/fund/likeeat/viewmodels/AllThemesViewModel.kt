package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.data.ReviewThemeLinkDao
import com.fund.likeeat.data.Theme
import com.fund.likeeat.data.ThemeRepository

class AllThemesViewModel internal constructor(
    themeRepository: ThemeRepository,
    // val linkDao: ReviewThemeLinkDao
    uid: Long
): ViewModel() {
    val themeList: LiveData<MutableList<Theme>> = themeRepository.getThemeList()
    val allRelations: MutableLiveData<List<ReviewThemeLink>> = MutableLiveData()


    /*fun getAllRelations(themeIdList: List<Long>) {
        allRelations.postValue(linkDao.getAllRelations(themeIdList))
    }*/
}
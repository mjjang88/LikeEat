package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel internal constructor(
    val reviewRepository: ReviewRepository,
    val themeDao: ThemeDao,
    val reviewThemeLinkDao: ReviewThemeLinkDao,
    kakaoFriendDao: KakaoFriendDao,
    friendLinkDao: FriendLinkDao,
    val uid: Long
) : ViewModel(){
    val review: LiveData<List<Review>> = reviewRepository.getReviewListByUid(uid)
    val theme: LiveData<List<Theme>> = themeDao.getThemeList(uid)

    val kakaofriends: LiveData<List<KakaoFriend>> = kakaoFriendDao.getKakaoFriends()
    val friendLink: LiveData<List<FriendLink>> = friendLinkDao.getFriendLink()
    val friends: MutableLiveData<List<KakaoFriend>> = MutableLiveData()
    val favoriteFriends: MutableLiveData<List<KakaoFriend>> = MutableLiveData()

    fun getFriendList() {

        val listFav: ArrayList<KakaoFriend> = ArrayList()
        val list: ArrayList<KakaoFriend> = ArrayList()

        viewModelScope.launch(Dispatchers.IO) {
            kakaofriends.value?.forEach { friend ->
                val isMatch = friendLink.value?.any {
                    friend.uid == it.uid_to
                }
                if (isMatch != null && isMatch == true) {
                    val isFav = friendLink.value?.find {
                        friend.uid == it.uid_to
                    }?.isFav

                    if (isFav != null && isFav == true) {
                        listFav.add(friend)
                    } else {
                        list.add(friend)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                friends.value = list
                favoriteFriends.value = listFav
            }
        }
    }
}
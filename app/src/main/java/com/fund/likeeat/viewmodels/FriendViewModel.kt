package com.fund.likeeat.viewmodels

import androidx.lifecycle.*
import com.fund.likeeat.data.FriendLink
import com.fund.likeeat.data.FriendLinkDao
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.data.KakaoFriendDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendViewModel internal constructor(
    kakaoFriendDao: KakaoFriendDao,
    friendLinkDao: FriendLinkDao,
    val uid: Long
) : ViewModel(){
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

    fun getFriendLinkById(FriendUid: Long): FriendLink? {
        return friendLink.value?.let {
            it.find {
                it.uid_to == FriendUid
            }
        }
    }
}
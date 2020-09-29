package com.fund.likeeat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.FriendLink
import com.fund.likeeat.data.FriendLinkDao
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.data.KakaoFriendDao
import com.fund.likeeat.network.LikeEatRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFriendViewModel internal constructor(
    kakaoFriendDao: KakaoFriendDao,
    friendLinkDao: FriendLinkDao,
    val uid: Long
) : ViewModel(){
    val kakaofriends: LiveData<List<KakaoFriend>> = kakaoFriendDao.getKakaoFriends()
    val friendLink: LiveData<List<FriendLink>> = friendLinkDao.getFriendLink()
    val friends: MutableLiveData<List<KakaoFriend>> = MutableLiveData()

    fun getFriendList() {

        val list: ArrayList<KakaoFriend> = ArrayList()

        viewModelScope.launch(Dispatchers.IO) {
            kakaofriends.value?.forEach { friend ->
                val isMatch = friendLink.value?.any {
                    friend.uid == it.uid_to
                }
                if (isMatch != null && isMatch == false) {
                    list.add(friend)
                }
            }

            withContext(Dispatchers.Main) {
                friends.value = list
            }
        }
    }

    suspend fun addFriend(friends: List<KakaoFriend>): Boolean {

        var isSuccess = true
        friends.forEach {
            val friendLink = FriendLink(-1, uid, it.uid, false)

            val result = viewModelScope.launch(Dispatchers.IO) {
                LikeEatRetrofit.getService().addFriends(friendLink).apply {
                    if (!isSuccessful) {
                        isSuccess = false
                    }
                }
            }
            result.join()
        }

        return isSuccess
    }
}
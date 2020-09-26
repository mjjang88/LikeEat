package com.fund.likeeat.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fund.likeeat.data.FriendLink
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.data.KakaoFriendDao
import com.fund.likeeat.network.LikeEatRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFriendViewModel internal constructor(
    kakaoFriendDao: KakaoFriendDao,
    val uid: Long
) : ViewModel(){
    val friends: LiveData<List<KakaoFriend>> = kakaoFriendDao.getKakaoFriends()

    suspend fun addFriend(friends: List<KakaoFriend>): Boolean {

        var isSuccess = true
        friends.forEach {
            val friendLink = FriendLink(uid, it.uid)

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
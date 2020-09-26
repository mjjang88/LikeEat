package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FriendLinkDao {
    @Query("SELECT * FROM friend_link")
    fun getFriendLink(): LiveData<List<FriendLink>>
}
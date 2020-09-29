package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface KakaoFriendDao {
    @Query("SELECT * FROM kakao_friend")
    fun getKakaoFriends(): LiveData<List<KakaoFriend>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friends: List<KakaoFriend>)

    @Query("DELETE FROM kakao_friend")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(friends: List<KakaoFriend>) {
        deleteAll()
        insertAll(friends)
    }
}
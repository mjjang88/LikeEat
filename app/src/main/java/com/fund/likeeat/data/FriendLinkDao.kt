package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FriendLinkDao {
    @Query("SELECT * FROM friend_link")
    fun getFriendLink(): LiveData<List<FriendLink>>

    @Query("SELECT * FROM friend_link WHERE uid_to = :friend_uid")
    fun getFriendLinkById(friend_uid: Long): FriendLink

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(friends: List<FriendLink>)

    @Query("DELETE FROM friend_link")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(friends: List<FriendLink>) {
        deleteAll()
        insertAll(friends)
    }
}
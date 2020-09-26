package com.fund.likeeat.data

import androidx.room.Entity
import com.google.gson.annotations.Expose

@Entity(tableName = "friend_link", primaryKeys = ["uid_from", "uid_to"])
data class FriendLink(
    @Expose
    val uid_from: Long,
    @Expose
    val uid_to: Long
) {
}
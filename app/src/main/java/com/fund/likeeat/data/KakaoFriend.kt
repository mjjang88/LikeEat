package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kakao_friend")
data class KakaoFriend(
    @PrimaryKey val uid: Long,
    val nickname: String?,
    val thumbnailUrl: String?
) {
}
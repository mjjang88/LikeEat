package com.fund.likeeat.data

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "friend_link", primaryKeys = ["uid_from", "uid_to"])
data class FriendLink(
    @Expose
    val uid_from: Long,
    @Expose
    val uid_to: Long,
    @Expose
    val isFav: Boolean
): Parcelable {
}
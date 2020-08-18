package com.fund.likeeat.data

import androidx.room.Entity

@Entity(tableName = "theme", primaryKeys = ["uid", "name"])
data class Theme(
    val uid: Long,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}
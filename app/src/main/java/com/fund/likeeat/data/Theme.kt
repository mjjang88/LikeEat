package com.fund.likeeat.data

import androidx.room.Entity

@Entity(tableName = "theme", primaryKeys = ["id"])
data class Theme(
    val id: Long,
    val uid: Long,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}
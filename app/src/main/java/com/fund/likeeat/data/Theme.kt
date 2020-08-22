package com.fund.likeeat.data

import androidx.room.Entity

@Entity(tableName = "theme", primaryKeys = ["uid", "name"])
data class Theme(
    val id: Long,
    val uid: Long,
    val reviewsCount: Int,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}

data class ThemeRequest(
    val uid: Long,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}
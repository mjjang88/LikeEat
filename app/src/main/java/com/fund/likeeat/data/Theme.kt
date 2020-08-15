package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme")
data class Theme(
    @PrimaryKey
    val id: Long,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}
package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey val id: Int,
    val name: String,
    val x: Double,
    val y: Double
) {
}
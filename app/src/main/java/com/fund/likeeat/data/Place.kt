package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey val id: Long,
    val uid: Long,
    val name: String,
    val oneLineReview: String,
    val x: Double,
    val y: Double
) {
}
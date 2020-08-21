package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "reviews")
data class Review(
    @SerializedName("id")
    @PrimaryKey val id: Long,
    val uid: Long,
    val lat: Double,
    val lng: Double,
    val isPublic: Boolean,
    val category: String?,
    val comment: String?,
    val visitedDayYmd: String?,
    val companions: String?,
    val toliets: String?,
    val priceRange: String?,
    val serviceQuality: String?,
    val themeIds: String?,
    val name: String,
    val address: String,
    val imageUri: String?
) {
}
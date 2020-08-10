package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "reviews")
data class Review(
    @SerializedName("id")
    @PrimaryKey val id: Long,
    val uid: Long,
    val name: String,
    val address: String,
    @SerializedName("comment")
    val oneLineReview: String,
    @SerializedName("lat")
    val x: Double,
    @SerializedName("lng")
    val y: Double,
    val imageUri: String
) {

}
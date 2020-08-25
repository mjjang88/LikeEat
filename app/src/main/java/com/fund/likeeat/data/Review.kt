package com.fund.likeeat.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "reviews")
data class Review(
    @SerializedName("id")
    @PrimaryKey val id: Long,
    val uid: Long,
    val isPublic: Boolean,
    val category: String?,
    val comment: String?,
    val visitedDayYmd: String?,
    val companions: String?,
    val toliets: String?,
    val priceRange: String?,
    val serviceQuality: String?,
    val revisit: String?,
    val imageUri: String?,

    val x: Double?,
    val y: Double?,
    val place_name: String?,
    val address_name: String?,
    val phone: String?
) : Parcelable {
}
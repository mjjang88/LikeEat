package com.fund.likeeat.network

import com.fund.likeeat.data.Place
import com.google.gson.annotations.Expose

data class ReviewNWWrite(
    @Expose
    val id: Long,
    @Expose
    val uid: Long,
    @Expose
    val isPublic: Boolean,
    @Expose
    val category: String?,
    @Expose
    val comment: String?,
    @Expose
    val visitedDayYmd: String?,
    @Expose
    val companions: String?,
    @Expose
    val toliets: String?,
    @Expose
    val priceRange: String?,
    @Expose
    val serviceQuality: String?,
    @Expose
    val revisit: String?,
    @Expose
    val themeIds: String?,
    @Expose
    val place: Place?,
    @Expose
    val imageUri: String?
) {
}
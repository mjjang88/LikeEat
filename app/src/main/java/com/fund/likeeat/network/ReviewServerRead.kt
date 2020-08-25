package com.fund.likeeat.network

import com.google.gson.annotations.Expose

class ReviewServerRead(
    @Expose
    val themes: List<ThemeServerRead>,
    @Expose
    val themesCount: Int,
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
    val id: Long,
    @Expose
    val place: PlaceServer?
) {
}
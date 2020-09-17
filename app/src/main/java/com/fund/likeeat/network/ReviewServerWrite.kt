package com.fund.likeeat.network

import com.google.gson.annotations.Expose

data class ReviewServerWrite(
    @Expose
    var isPublic: Boolean,
    @Expose
    var category: String?,
    @Expose
    var comment: String?,
    @Expose
    var visitedDayYmd: String?,
    @Expose
    var companions: String?,
    @Expose
    var toliets: String?,
    @Expose
    var priceRange: String?,
    @Expose
    var serviceQuality: String?,
    @Expose
    var themeIds: String?,
    @Expose
    var uid: Long,
    @Expose
    var revisit: String?,
    @Expose
    var place: PlaceServer?
) {
}
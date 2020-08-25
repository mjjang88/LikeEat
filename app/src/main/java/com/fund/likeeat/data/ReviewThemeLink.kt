package com.fund.likeeat.data

import androidx.room.Entity

@Entity(tableName = "review_theme_link", primaryKeys = ["reviewId", "themeId"])
data class ReviewThemeLink(
    val reviewId: Long,
    val themeId: Long
) {
}
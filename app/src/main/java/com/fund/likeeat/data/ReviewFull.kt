package com.fund.likeeat.data

data class ReviewFull (
    val id: Long,
    val review: Review,
    val theme: List<Theme>
) {
}
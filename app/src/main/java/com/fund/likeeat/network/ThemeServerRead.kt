package com.fund.likeeat.network

import com.google.gson.annotations.Expose

class ThemeServerRead(
    @Expose
    val id: Long,
    @Expose
    val uid: Long,
    @Expose
    val name: String?,
    @Expose
    val color: String?,
    @Expose
    val isPublic: String?
) {
}
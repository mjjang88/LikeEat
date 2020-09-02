package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ThemeDao {
    @Query("SELECT * FROM theme WHERE uid=:uid")
    fun getThemeList(uid: Long): LiveData<List<Theme>>

    @Query("SELECT * FROM theme WHERE uid=:uid")
    fun getThemeList2(uid: Long): List<Theme>

    @Query("SELECT * FROM theme a, review_theme_link b WHERE a.id = b.themeId AND b.reviewId in (:reviewId)")
    suspend fun getThemeListByReviewId(reviewId: Long): List<Theme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(list: List<Theme>?)
}
package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ThemeDao {
    @Query("SELECT * FROM theme")
    fun getThemeList(): LiveData<MutableList<Theme>>

    @Query("SELECT * FROM theme WHERE uid=:uid")
    fun getThemeList(uid: Long): LiveData<List<Theme>>

    @Query("SELECT * FROM theme WHERE uid=:uid")
    fun getThemeList2(uid: Long): List<Theme>

    @Query("SELECT * FROM theme a, review_theme_link b WHERE a.id = b.themeId AND b.reviewId in (:reviewId)")
    suspend fun getThemeListByReviewId(reviewId: Long): List<Theme>

    @Query("SELECT * FROM theme WHERE id=:id")
    fun getTheme(id: Long): LiveData<Theme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(list: List<Theme>?)

    @Query("UPDATE theme SET name=:name, color=:color, isPublic=:isPublic WHERE id=:id")
    suspend fun updateTheme(id: Long, name: String, color: Int, isPublic: Boolean)

    @Query("DELETE FROM theme WHERE id=:id")
    suspend fun deleteTheme(id: Long)

    @Query("SELECT * FROM theme WHERE id=:themeId")
    fun getThemeByThemeId(themeId: Long): Theme
}
package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ThemeDao {
    @Query("SELECT * FROM theme")
    fun getThemeList(): LiveData<List<Theme>>

    @Query("SELECT * FROM theme WHERE uid=:uid")
    suspend fun getThemeList2(uid: Long): List<Theme>

    @Query("SELECT * FROM theme WHERE id=:id")
    fun getTheme(id: Long): LiveData<Theme>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(list: List<Theme>?)

    @Query("UPDATE theme SET name=:name, color=:color, isPublic=:isPublic WHERE id=:id")
    suspend fun updateTheme(id: Long, name: String, color: Int, isPublic: Boolean)

    @Query("DELETE FROM theme WHERE id=:id")
    suspend fun deleteTheme(id: Long)
}
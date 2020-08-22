package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
}
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTheme(list: Theme)
}
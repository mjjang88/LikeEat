package com.fund.likeeat.data

import androidx.room.*

@Dao
interface ReviewThemeLinkDao {

    @Query("SELECT * FROM review_theme_link")
    fun getList(): List<ReviewThemeLink>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ReviewThemeLink>)

    @Query("DELETE FROM review_theme_link")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(list: List<ReviewThemeLink>) {
        deleteAll()
        insertAll(list)
    }
}
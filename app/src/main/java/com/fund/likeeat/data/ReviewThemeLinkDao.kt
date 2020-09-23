package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewThemeLinkDao {

    @Query("SELECT * FROM review_theme_link")
    fun getList(): List<ReviewThemeLink>

    @Query("SELECT * FROM review_theme_link WHERE themeId = :themeId")
    fun getListByThemeId(themeId: Long): LiveData<List<ReviewThemeLink>>

    @Query("SELECT * FROM review_theme_link WHERE reviewId = :reviewId")
    fun getListByReviewId(reviewId: Long): LiveData<List<ReviewThemeLink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ReviewThemeLink>)

    @Query("DELETE FROM review_theme_link")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(list: List<ReviewThemeLink>) {
        deleteAll()
        insertAll(list)
    }

    @Query("DELETE FROM review_theme_link WHERE reviewId = :reviewId AND themeId = :themeId")
    suspend fun deleteOneRelation(reviewId: Long, themeId: Long)
}
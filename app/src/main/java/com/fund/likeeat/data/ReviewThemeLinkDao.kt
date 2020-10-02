package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ReviewThemeLinkDao {

    @Query("SELECT * FROM review_theme_link")
    fun getList(): List<ReviewThemeLink>

    @Query("SELECT * FROM review_theme_link WHERE themeId = :themeId")
    fun getListByThemeId(themeId: Long): LiveData<List<ReviewThemeLink>>

    @Query("SELECT * FROM review_theme_link WHERE reviewId = :reviewId")
    fun getListByReviewId(reviewId: Long): List<ReviewThemeLink>

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

    @Query("DELETE FROM review_theme_link WHERE reviewId = :reviewId AND themeId in (:themeIds)")
    suspend fun deleteRelations(reviewId: Long, themeIds: List<Long>)

    @Query("UPDATE review_theme_link SET themeId = :newThemeId WHERE reviewId = :reviewId AND themeId = :themeId")
    suspend fun updateOneRelation(reviewId: Long, themeId: Long, newThemeId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneRelation(relation: ReviewThemeLink)

    @Query("SELECT * FROM review_theme_link WHERE themeId = :themeId")
    fun getReviewListByThemeId(themeId: Long): List<ReviewThemeLink>
}
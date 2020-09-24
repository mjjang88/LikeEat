package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews")
    fun getReviewList(): LiveData<List<Review>>

    @Query("SELECT * FROM reviews")
    fun getReviewList2(): List<Review>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matchs: List<Review>)

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAndInsertAll(matchs: List<Review>) {
        deleteAll()
        insertAll(matchs)
    }

    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    fun getReviewById(reviewId: Long): LiveData<Review>

    @Query("SELECT * FROM reviews WHERE id in (:reviewId)")
    fun getReviewByTheme(reviewId: List<Long>): List<Review>

    @Query("SELECT * FROM reviews WHERE uid = :uid AND ((place_name LIKE :word) OR (address_name LIKE :word))")
    fun getReviewByUidAndWord(uid: Long, word: String): MutableList<Review>
}
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

    @Query("SELECT * FROM reviews WHERE uid = :uid GROUP BY x, y, place_name")
    fun getReviewListByUid(uid: Long): LiveData<List<Review>>

    @Query("SELECT * FROM reviews WHERE uid = :uid AND place_name = :placeName AND address_name = :addressName")
    fun getReviewListBySamePlace(uid: Long, placeName: String, addressName: String): LiveData<List<Review>>

    @Query("SELECT * FROM reviews WHERE place_name = :placeName AND address_name = :addressName")
    fun getReviewListBySamePlace2(placeName: String, addressName: String): List<Review>

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

    @Query("SELECT * FROM reviews WHERE id in (:reviewId) GROUP BY x, y, place_name")
    fun getReviewByReviewIdWithNoSameData(reviewId: List<Long>): List<Review>

    @Query("SELECT * FROM reviews WHERE uid = :uid AND ((place_name LIKE :word) OR (address_name LIKE :word)) GROUP BY x, y, place_name")
    fun getReviewByUidAndWord(uid: Long, word: String): MutableList<Review>

    @Query("SELECT * FROM reviews WHERE uid = :uid AND x = :x AND y = :y AND place_name = :placeName")
    fun getReviewListByPlace(uid: Long, x: Double, y: Double, placeName: String): List<Review>

    @Query("SELECT * FROM reviews WHERE uid = :uid GROUP BY x, y, place_name")
    fun getPlaceCount(uid: Long): LiveData<List<Review>>
}
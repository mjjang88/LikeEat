package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews")
    fun getReviewList(): LiveData<List<Review>>

    @Query("SELECT * FROM reviews")
    fun getReviewList2(): List<Review>

    @Query("SELECT * FROM reviews WHERE place_name = :placeName AND address_name = :addressName")
    fun getReviewListBySamePlace(placeName: String, addressName: String): LiveData<List<Review>>

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
}
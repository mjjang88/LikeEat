package com.fund.likeeat.data

import androidx.lifecycle.LiveData
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
}
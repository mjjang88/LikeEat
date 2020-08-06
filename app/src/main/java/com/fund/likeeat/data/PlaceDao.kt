package com.fund.likeeat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getPlaceList(): LiveData<List<Place>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matchs: List<Place>)

    // 잠깐 테스트용
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)
}
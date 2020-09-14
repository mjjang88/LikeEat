package com.fund.likeeat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fund.likeeat.manager.MyApplication

@Entity(tableName = "theme")
data class Theme(
    @PrimaryKey val id: Long,
    val uid: Long = MyApplication.pref.uid,
    val reviewsCount: Int,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}

data class ThemeRequest(
    val uid: Long,
    val name: String,
    val color: Int,
    val isPublic: Boolean
) {

}

data class ThemeChanged(
    val name: String,
    val color: Int,
    val isPublic: Boolean
){
    
}
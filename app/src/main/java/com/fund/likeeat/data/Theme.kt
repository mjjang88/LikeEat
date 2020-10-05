package com.fund.likeeat.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fund.likeeat.manager.MyApplication
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "theme")
data class Theme(
    @PrimaryKey val id: Long,
    val uid: Long = MyApplication.pref.uid,
    val reviewsCount: Int,
    val name: String,
    val color: Int,
    val isPublic: Boolean
): Parcelable {

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
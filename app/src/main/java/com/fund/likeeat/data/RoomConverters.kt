package com.fund.likeeat.data

import androidx.room.TypeConverter

class RoomConverters {
    @TypeConverter
    fun fromString(stringListString: String): List<Int> {
        return stringListString.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(stringList: List<Int>): String {
        return stringList.joinToString(separator = ",")
    }
}
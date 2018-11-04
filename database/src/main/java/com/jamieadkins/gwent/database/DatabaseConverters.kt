package com.jamieadkins.gwent.database

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.*

class DatabaseConverters {
    private var gson = Gson()

    @TypeConverter
    fun stringToList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToMap(data: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(data, mapType)
    }

    @TypeConverter
    fun mapToString(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun stringToIntMap(data: String): Map<String, Int> {
        val mapType = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(data, mapType)
    }

    @TypeConverter
    fun intMapToString(map: Map<String, Int>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}
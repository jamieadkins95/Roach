package com.jamieadkins.gwent.database

import android.arch.persistence.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.Collections

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
}
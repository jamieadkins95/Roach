package com.jamieadkins.gwent.database

import android.arch.persistence.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.Collections

class MapConverters {
    private var gson = Gson()

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
package com.jamieadkins.gwent.data

abstract class Mapper<From, To : Any> {

    abstract fun map(from: From): To

    fun mapList(from: List<From>): List<To> {
        return from.mapNotNull {
            try {
               map(it)
            } catch (e: Exception) {
                null as To?
            }
        }
    }
}
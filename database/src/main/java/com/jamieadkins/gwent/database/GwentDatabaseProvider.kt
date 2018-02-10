package com.jamieadkins.gwent.database

import android.arch.persistence.room.Room
import android.content.Context

object GwentDatabaseProvider {

    var instance: GwentDatabase? = null

    fun getDatabase(applicationContext: Context): GwentDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(applicationContext, GwentDatabase::class.java, GwentDatabase.DB_NAME).build()
        }

        return instance as GwentDatabase
    }
}
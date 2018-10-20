package com.jamieadkins.gwent.database

import androidx.room.Room
import android.content.Context

object GwentDatabaseProvider {

    private var instance: GwentDatabase? = null

    fun getDatabase(applicationContext: Context): GwentDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(applicationContext, GwentDatabase::class.java, GwentDatabase.DB_NAME).build()
        }

        return instance as GwentDatabase
    }
}
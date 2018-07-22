package com.jamieadkins.gwent.main

import android.app.Application
import android.util.Log
import com.jamieadkins.gwent.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

class GwentApplication : Application() {

    companion object {
        lateinit var INSTANCE: GwentApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
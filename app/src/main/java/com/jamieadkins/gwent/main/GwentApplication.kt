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
            Timber.plant(CrashReportingTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            Crashlytics.log(priority, tag, message)

            if (t != null && priority == Log.ERROR) {
                Crashlytics.logException(t)
            }
        }
    }
}
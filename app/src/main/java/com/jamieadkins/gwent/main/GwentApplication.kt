package com.jamieadkins.gwent.main

import android.content.Context
import com.jamieadkins.gwent.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import androidx.multidex.MultiDex
import com.jamieadkins.gwent.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree



class GwentApplication : DaggerApplication() {

    companion object {
        lateinit var INSTANCE: GwentApplication
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            // TODO Report to crashlytics
            // Timber.plant(CrashReportingTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
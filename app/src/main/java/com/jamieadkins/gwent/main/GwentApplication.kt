package com.jamieadkins.gwent.main

import android.content.Context
import com.jamieadkins.gwent.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.jamieadkins.gwent.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree
import io.fabric.sdk.android.Fabric

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
        Fabric.with(this, Crashlytics())

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
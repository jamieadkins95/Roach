package com.jamieadkins.gwent.base

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import timber.log.Timber.DebugTree

class GwentApplication : MultiDexApplication() {

    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .context(context = this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    companion object {
        val Context.coreComponent
            get() = (applicationContext as GwentApplication).coreComponent
    }
}
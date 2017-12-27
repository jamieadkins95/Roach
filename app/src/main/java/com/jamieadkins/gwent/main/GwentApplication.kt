package com.jamieadkins.gwent.main

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class GwentApplication : Application() {

    companion object {
        lateinit var INSTANCE: GwentApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Fabric.with(this, Crashlytics())
    }
}
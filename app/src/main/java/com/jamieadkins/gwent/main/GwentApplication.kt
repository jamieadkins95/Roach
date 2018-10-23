package com.jamieadkins.gwent.main

import android.content.Context
import com.jamieadkins.gwent.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import androidx.multidex.MultiDex

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
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
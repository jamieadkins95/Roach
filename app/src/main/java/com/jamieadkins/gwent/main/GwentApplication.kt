package com.jamieadkins.gwent.main

import com.jamieadkins.gwent.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class GwentApplication : DaggerApplication() {

    companion object {
        lateinit var INSTANCE: GwentApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}
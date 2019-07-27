package com.jamieadkins.gwent.base

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
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

        initErrorHandling()
    }

    /**
     * Rx can trigger crashes if exceptions occur after an Observable stream has completed/disposed.
     * Generally, we don't care about them, they are usually IOExceptions. For example, NYTimes Store
     * will try to write data to disk after the Observable stream has completed.
     *
     * https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
     */
    private fun initErrorHandling() {
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                // Do nothing, we don't have to worry about these.
            } else {
                Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
            }
        }
    }

    companion object {
        val Context.coreComponent
            get() = (applicationContext as GwentApplication).coreComponent
    }
}
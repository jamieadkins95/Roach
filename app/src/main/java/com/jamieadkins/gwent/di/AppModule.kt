package com.jamieadkins.gwent.di

import android.app.NotificationManager
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.main.SchedulerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

@Module
abstract class AppModule {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
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

    @Binds
    abstract fun provideContext(application: GwentApplication): Context

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun resources(context: Context): Resources = context.resources

        @Provides
        @JvmStatic
        fun schedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

        @Provides
        @JvmStatic
        fun baseSchedulerProvider(): BaseSchedulerProvider = com.jamieadkins.commonutils.mvp2.SchedulerProvider

        @Provides
        @JvmStatic
        fun notificationManager(context: Context): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        @Provides
        @JvmStatic
        fun assetManager(context: Context): AssetManager = context.assets
    }
}
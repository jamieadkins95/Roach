package com.jamieadkins.gwent.base

import android.content.Context
import android.content.res.Resources
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.SchedulerProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class,
        SchedulerModule::class,
        PreferencesModule::class,
        ResourcesModule::class
    ])
@Singleton
interface CoreComponent {

    fun exposeContext(): Context

    fun exposeDatabase(): GwentDatabase

    fun exposeSchedulerProvider(): SchedulerProvider

    fun exposeRxPreferences(): RxSharedPreferences

    fun exposeResources(): Resources

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): CoreComponent
    }
}
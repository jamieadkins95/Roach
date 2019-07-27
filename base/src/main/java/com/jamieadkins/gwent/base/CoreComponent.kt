package com.jamieadkins.gwent.base

import android.content.Context
import com.jamieadkins.gwent.database.GwentDatabase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        DatabaseModule::class
    ])
@Singleton
interface CoreComponent {

    fun exposeContext(): Context

    fun exposeDatabase(): GwentDatabase

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): CoreComponent
    }
}
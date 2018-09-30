package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.main.GwentApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FragmentInjectionModule::class,
        AppModule::class
    ]
)
@Singleton
abstract class AppComponent : AndroidInjector<GwentApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GwentApplication>()
}
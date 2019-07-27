package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.base.CoreComponent
import com.jamieadkins.gwent.card.data.CardDataModule
import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.di.ActivityScoped
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        UpdateModule::class,
        UpdateDataModule::class,
        CardDataModule::class
    ],
    dependencies = [CoreComponent::class]
)
@ActivityScoped
interface UpdateComponent {

    fun inject(target: UpdateService)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        @BindsInstance
        fun service(service: UpdateService): Builder

        fun build(): UpdateComponent
    }
}
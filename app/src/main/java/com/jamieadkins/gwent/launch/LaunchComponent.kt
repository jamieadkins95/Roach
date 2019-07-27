package com.jamieadkins.gwent.launch

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
        LaunchModule::class,
        UpdateDataModule::class,
        CardDataModule::class
    ],
    dependencies = [CoreComponent::class]
)
@ActivityScoped
interface LaunchComponent {

    fun inject(target: LaunchActivity)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        @BindsInstance
        fun activity(activity: LaunchActivity): Builder

        fun build(): LaunchComponent
    }
}
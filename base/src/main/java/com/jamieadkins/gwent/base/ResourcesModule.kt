package com.jamieadkins.gwent.base

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
class ResourcesModule {

    @Provides
    fun resources(context: Context): Resources = context.resources
}
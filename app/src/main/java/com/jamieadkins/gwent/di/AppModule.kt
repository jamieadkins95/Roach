package com.jamieadkins.gwent.di

import android.app.NotificationManager
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.base.SchedulerProviderImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun notificationManager(context: Context): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun assetManager(context: Context): AssetManager = context.assets
}
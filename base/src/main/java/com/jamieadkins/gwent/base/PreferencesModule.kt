package com.jamieadkins.gwent.base

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun preferences(context: Context): RxSharedPreferences {
        return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(context))
    }
}
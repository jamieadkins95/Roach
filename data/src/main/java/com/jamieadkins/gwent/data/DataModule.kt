package com.jamieadkins.gwent.data

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.Reusable
import java.io.File
import javax.inject.Named

@Module
class DataModule {

    @Provides
    @Reusable
    fun preferences(context: Context): RxSharedPreferences {
        return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(context))
    }

    @Provides
    @Named("files")
    fun filesDir(context: Context): File = context.filesDir

    @Provides
    @Named("cache")
    fun cacheDir(context: Context): File = context.cacheDir

    @Provides
    fun firestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
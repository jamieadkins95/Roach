package com.jamieadkins.gwent

import android.util.Log
import com.nytimes.android.external.store3.base.impl.BarCode
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import okio.BufferedSource
import com.jamieadkins.gwent.main.GwentApplication
import com.nytimes.android.external.fs3.SourcePersisterFactory
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.base.impl.Store
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jamieadkins.gwent.data.FileSystemRecordClearingPersister
import com.nytimes.android.external.fs3.PathResolver
import io.reactivex.Flowable
import okhttp3.ResponseBody
import java.lang.reflect.Type
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import com.nytimes.android.external.fs3.RecordPersister

object StoreManager {
    private val storeMap = hashMapOf<BarCode, Store<*, BarCode>>()

    fun generateId(vararg args: Any): String {
        var id = ""
        for (obj in args) {
            id += obj
        }
        return id
    }

    fun <T> getData(barCode: BarCode, observable: Single<ResponseBody>, type: Type, expirationTime: Long): Flowable<T> {
        var store = storeMap[barCode] as Store<T, BarCode>?
        if (store == null) {
            val storeBuilder = StoreBuilder.parsedWithKey<BarCode, BufferedSource, T>()
                    .fetcher { observable.map(ResponseBody::source) }
                    .memoryPolicy(MemoryPolicy
                            .builder()
                            .setExpireAfterWrite(expirationTime)
                            .setExpireAfterTimeUnit(TimeUnit.SECONDS)
                            .build())
                    .parser(GsonParserFactory.createSourceParser<T>(provideGson(), type))
            try {
                storeBuilder.persister(SourcePersisterFactory.create(GwentApplication.INSTANCE.applicationContext.cacheDir))
            } catch (e: Exception) {
                Log.e(StoreManager.javaClass.simpleName, "Failed to get file store.", e)
            }

            store = storeBuilder.open()

            storeMap.put(barCode, store)

        }

        return store.get(barCode)
                .concatWith(store.fetch(barCode))
                .distinct()
    }

    fun <T> getDataOnce(barCode: BarCode, observable: Single<ResponseBody>, type: Type, expirationTime: Long): Single<T> {
        var store = storeMap[barCode] as Store<T, BarCode>?
        if (store == null) {
            store = StoreBuilder.parsedWithKey<BarCode, BufferedSource, T>()
                    .fetcher { observable.map(ResponseBody::source) }
                    .networkBeforeStale()
                    .memoryPolicy(MemoryPolicy
                            .builder()
                            .setExpireAfterWrite(expirationTime)
                            .setExpireAfterTimeUnit(TimeUnit.SECONDS)
                            .build())
                    .parser(GsonParserFactory.createSourceParser<T>(provideGson(), type))
                    .persister(FileSystemRecordClearingPersister.create(
                            FileSystemFactory.create(GwentApplication.INSTANCE.cacheDir),
                            BarCodePathResolver(),
                            expirationTime,
                            TimeUnit.SECONDS))
                    .open()

            storeMap.put(barCode, store)
        }

        return store.get(barCode)
    }

    fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    fun <T> genericType() = object : TypeToken<T>() {}.type

    private class BarCodePathResolver : PathResolver<BarCode> {
        override fun resolve(key: BarCode): String {
            return key.toString()
        }
    }
}
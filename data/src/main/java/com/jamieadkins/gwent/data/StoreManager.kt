package com.jamieadkins.gwent.data

import com.nytimes.android.external.store3.base.impl.BarCode
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import okio.BufferedSource
import com.nytimes.android.external.fs3.SourcePersisterFactory
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.base.impl.Store
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.nytimes.android.external.fs3.PathResolver
import io.reactivex.Flowable
import okhttp3.ResponseBody
import java.lang.reflect.Type
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import java.io.File

class StoreManager(val cacheDirectory: File) {
    private val storeMap = hashMapOf<BarCode, Store<*, BarCode>>()

    fun <T> getData(barCode: BarCode, observable: Single<ResponseBody>, type: Type, expirationTime: Long): Flowable<T> {
        var store = storeMap[barCode] as Store<T, BarCode>?
        if (store == null) {
            store = StoreBuilder.parsedWithKey<BarCode, BufferedSource, T>()
                    .fetcher { observable.map(ResponseBody::source) }
                    .memoryPolicy(MemoryPolicy
                            .builder()
                            .setExpireAfterWrite(expirationTime)
                            .setExpireAfterTimeUnit(TimeUnit.SECONDS)
                            .build())
                    .parser(GsonParserFactory.createSourceParser<T>(provideGson(), type))
                    .persister(SourcePersisterFactory.create(cacheDirectory))
                    .open()

            storeMap[barCode] = store

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
                            FileSystemFactory.create(cacheDirectory),
                            BarCodePathResolver(),
                            expirationTime,
                            TimeUnit.SECONDS))
                    .open()

            storeMap.put(barCode, store)
        }

        return store.get(barCode)
    }

    companion object {
        fun generateId(vararg args: Any): String {
            var id = ""
            for (obj in args) {
                id += obj
            }
            return id
        }

        fun provideGson(): Gson {
            return GsonBuilder()
                    .create()
        }

        private class BarCodePathResolver : PathResolver<BarCode> {
            override fun resolve(key: BarCode): String {
                return key.toString()
            }
        }
    }
}
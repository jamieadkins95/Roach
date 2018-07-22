package com.jamieadkins.gwent.data

import android.util.Log

import com.nytimes.android.external.fs3.FSReader
import com.nytimes.android.external.fs3.FSWriter
import com.nytimes.android.external.fs3.PathResolver
import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.store3.base.Clearable
import com.nytimes.android.external.store3.base.Persister
import com.nytimes.android.external.store3.base.RecordProvider
import com.nytimes.android.external.store3.base.RecordState

import java.io.IOException
import java.util.concurrent.TimeUnit

import io.reactivex.Maybe
import io.reactivex.Single
import okio.BufferedSource

class FileSystemRecordClearingPersister<Key> private constructor(private val fileSystem: FileSystem, private val pathResolver: PathResolver<Key>,
                                                                 private val expirationDuration: Long,
                                                                 private val expirationUnit: TimeUnit) : Persister<BufferedSource, Key>,
                                                                                                         RecordProvider<Key>,
                                                                                                         Clearable<Key> {

    private val fileReader: FSReader<Key> = FSReader(fileSystem, pathResolver)
    private val fileWriter: FSWriter<Key> = FSWriter(fileSystem, pathResolver)

    override fun getRecordState(key: Key): RecordState {
        return this.fileSystem.getRecordState(this.expirationUnit,
                                              this.expirationDuration,
                                              this.pathResolver.resolve(key))
    }

    override fun read(key: Key): Maybe<BufferedSource> {
        return this.fileReader.read(key)
    }

    override fun write(key: Key, bufferedSource: BufferedSource): Single<Boolean> {
        return this.fileWriter.write(key, bufferedSource)
    }

    override fun clear(key: Key) {
        try {
            fileSystem.deleteAll(pathResolver.resolve(key))
        } catch (e: IOException) {
            Log.e("don't worry about it", ":-)")
        }
    }

    companion object {
        fun <T> create(fileSystem: FileSystem?,
                       pathResolver: PathResolver<T>,
                       expirationDuration: Long,
                       expirationUnit: TimeUnit): FileSystemRecordClearingPersister<T> {
            return if (fileSystem == null) {
                throw IllegalArgumentException("root file cannot be null.")
            } else {
                FileSystemRecordClearingPersister(fileSystem, pathResolver,
                                                  expirationDuration, expirationUnit)
            }
        }
    }
}
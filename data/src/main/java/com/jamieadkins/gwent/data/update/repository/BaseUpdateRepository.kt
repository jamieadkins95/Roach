package com.jamieadkins.gwent.data.update.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.nytimes.android.external.cache3.Cache
import com.nytimes.android.external.cache3.CacheBuilder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseUpdateRepository(
    private val filesDirectory: File,
    private val preferences: RxSharedPreferences) : UpdateRepository {

    private val storage = FirebaseStorage.getInstance()
    val gson = Gson()
    private val cache: Cache<String, Single<Long>> = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build()

    val updateStateChanges = BehaviorSubject.createDefault(Any())

    abstract val FILE_NAME: String

    override fun hasDoneFirstTimeSetup(): Observable<Boolean> {
        return preferences.getBoolean("first-time-setup-$FILE_NAME", false).asObservable()
    }

    override fun performFirstTimeSetup(): Completable {
        return hasDoneFirstTimeSetup()
            .first(false)
            .flatMapCompletable { doneSetup ->
                if (!doneSetup) {
                    internalPerformFirstTimeSetup()
                        .doOnComplete { preferences.getBoolean("first-time-setup-$FILE_NAME").set(true) }
                } else {
                    Completable.complete()
                }
            }
    }

    override fun performUpdate(): Completable {
        return isUpdateAvailable()
            .first(false)
            .flatMapCompletable { updateAvailable ->
                if (updateAvailable) {
                    internalPerformUpdate()
                        .doOnComplete { updateStateChanges.onNext(Any()) }
                } else {
                    Completable.complete()
                }
            }
    }

    abstract fun internalPerformUpdate(): Completable

    abstract fun internalPerformFirstTimeSetup(): Completable

    protected fun getRemoteLastUpdated(patch: String, fileName: String): Single<Long> {
        val storageRef = getStorageReference(patch, fileName)
        return cache.get(storageRef.path) {
            getMetaData(storageRef)
                .map { it.updatedTimeMillis }
        }!!
    }

    protected fun getLocalLastUpdated(patch: String, fileName: String): Single<Long> {
        return preferences.getLong(patch + fileName).asObservable().firstOrError()
    }

    protected fun updateLocalLastUpdated(patch: String, fileName: String): Completable {
        return Completable.fromCallable {
            preferences.getLong(patch + fileName).set(Date().time)
        }
    }

    protected fun getFileFromFirebase(storageRef: StorageReference, outputFileName: String): Single<File> {
        return Single.create<File> { emitter ->
            val file = File(filesDirectory, outputFileName)
            val taskSnapshotStorageTask = storageRef.getFile(file)
                .addOnSuccessListener { _ ->
                    emitter.onSuccess(file)
                }
                .addOnFailureListener { e ->
                    if (!emitter.isDisposed) {
                        emitter.onError(e)
                    }
                }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    protected fun getMetaData(storageRef: StorageReference): Single<StorageMetadata> {
        return Single.create<StorageMetadata> { emitter ->
            storageRef.metadata
                .addOnSuccessListener { taskSnapshot ->
                    emitter.onSuccess(taskSnapshot)
                }
                .addOnFailureListener { e ->
                    if (!emitter.isDisposed) {
                        emitter.onError(e)
                    }
                }
        }
    }

    protected fun getStorageReference(patch: String, fileName: String): StorageReference {
        return storage.reference.child("card-data").child(patch).child(fileName)
    }

    protected fun <T> parseJsonFile(file: File, type: Type = genericType<T>()): Single<T> {
        return Single.fromCallable {
            val bufferedReader = BufferedReader(FileReader(file))
            gson.fromJson<T>(bufferedReader, type)
        }
    }

    fun <T> genericType() = object : TypeToken<T>() {}.type
}
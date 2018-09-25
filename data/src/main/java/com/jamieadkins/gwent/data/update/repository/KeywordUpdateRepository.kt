package com.jamieadkins.gwent.data.update.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import com.jamieadkins.gwent.database.GwentDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.jamieadkins.gwent.data.update.mapper.GwentKeywordMapper
import com.jamieadkins.gwent.data.update.model.FirebaseKeywordResult
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction

class KeywordUpdateRepository(private val database: GwentDatabase,
                              filesDirectory: File,
                              private val patchRepository: PatchRepository,
                              preferences: RxSharedPreferences) : BaseUpdateRepository(filesDirectory, preferences), UpdateRepository {

    private companion object {
        const val FILE_NAME = "keywords.json"
    }

    override fun isUpdateAvailable(): Single<Boolean> {
        return patchRepository.getLatestPatchId()
            .flatMap { patch ->
                Single.zip(
                    getRemoteLastUpdated(patch, FILE_NAME),
                    getLocalLastUpdated(patch, FILE_NAME),
                    BiFunction { remote: Long, local: Long ->
                        remote > local
                    }
                )
            }
            .onErrorReturnItem(false)
    }

    override fun performFirstTimeSetup(): Observable<UpdateResult> {
        return Observable.empty()
    }

    override fun internalPerformUpdate(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMap { getFileFromFirebase(getStorageReference(it, FILE_NAME), FILE_NAME) }
            .observeOn(Schedulers.io())
            .flatMap { parseJsonFile<FirebaseKeywordResult>(it, FirebaseKeywordResult::class.java) }
            .flatMapCompletable { updateKeywordDatabase(it) }
            .andThen(updateLastUpdated())
    }

    private fun updateKeywordDatabase(result: FirebaseKeywordResult): Completable {
        return Completable.fromCallable {
            val keywords = GwentKeywordMapper.mapToKeywordEntityList(result)
            database.keywordDao().insert(keywords)
        }
    }

    private fun updateLastUpdated(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMapCompletable { updateLocalLastUpdated(it, FILE_NAME) }
    }
}
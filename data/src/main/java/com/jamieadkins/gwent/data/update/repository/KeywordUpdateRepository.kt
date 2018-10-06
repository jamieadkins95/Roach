package com.jamieadkins.gwent.data.update.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.data.update.mapper.GwentKeywordMapper
import com.jamieadkins.gwent.data.update.model.FirebaseKeywordResult
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class KeywordUpdateRepository @Inject constructor(
    private val database: GwentDatabase,
    @Named("files") filesDirectory: File,
    private val patchRepository: PatchRepository,
    preferences: RxSharedPreferences) : BaseUpdateRepository(filesDirectory, preferences), UpdateRepository {

    private companion object {
        const val FILE_NAME = "keywords.json"
    }

    override fun isUpdateAvailable(): Observable<Boolean> {
        return updateStateChanges
            .flatMapSingle {
                patchRepository.getLatestPatchId()
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
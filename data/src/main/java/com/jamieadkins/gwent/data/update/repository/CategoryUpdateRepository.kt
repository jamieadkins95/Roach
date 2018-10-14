package com.jamieadkins.gwent.data.update.repository

import android.content.res.AssetManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.data.update.mapper.GwentCategoryMapper
import com.jamieadkins.gwent.data.update.model.FirebaseCategoryResult
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named

class CategoryUpdateRepository @Inject constructor(
    private val database: GwentDatabase,
    @Named("files") filesDirectory: File,
    private val patchRepository: PatchRepository,
    preferences: RxSharedPreferences,
    private val assetManager: AssetManager
) : BaseUpdateRepository(filesDirectory, preferences), UpdateRepository {

    override val FILE_NAME = "categories.json"

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

    override fun internalPerformFirstTimeSetup(): Completable {
        return Single.fromCallable {
            with(assetManager.open(FILE_NAME)) {
                val reader = InputStreamReader(this, "UTF-8")
                gson.fromJson<FirebaseCategoryResult>(reader, FirebaseCategoryResult::class.java)

            }
        }.flatMapCompletable(::updateCategoriesDatabase)
    }

    override fun internalPerformUpdate(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMap { getFileFromFirebase(getStorageReference(it, FILE_NAME), FILE_NAME) }
            .observeOn(Schedulers.io())
            .flatMap { parseJsonFile<FirebaseCategoryResult>(it, FirebaseCategoryResult::class.java) }
            .flatMapCompletable { updateCategoriesDatabase(it) }
            .andThen(updateLastUpdated())
    }

    private fun updateCategoriesDatabase(result: FirebaseCategoryResult): Completable {
        return Completable.fromCallable {
            val categories = GwentCategoryMapper.mapToCategoryEntityList(result)
            database.categoryDao().insert(categories)
        }
    }

    private fun updateLastUpdated(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMapCompletable { updateLocalLastUpdated(it, FILE_NAME) }
    }
}
package com.jamieadkins.gwent.data.update.repository

import com.jamieadkins.gwent.data.update.mapper.GwentCategoryMapper
import com.jamieadkins.gwent.data.update.model.FirebaseCategoryResult
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.io.File

class CategoryUpdateRepository(private val database: GwentDatabase,
                               filesDirectory: File,
                               private val patchRepository: PatchRepository) : BaseUpdateRepository(filesDirectory), UpdateRepository {

    private companion object {
        const val FILE_NAME = "categories.json"
    }

    override fun isUpdateAvailable(): Single<Boolean> {
        return patchRepository.getLatestPatchId()
            .flatMapMaybe { patch ->
                Maybe.zip(
                    getRemoteLastUpdated(patch, FILE_NAME).toMaybe(),
                    database.patchDao().getPatchVersion(patch).map { it.lastUpdated },
                    BiFunction { remote: Long, local: Long ->
                        remote > local
                    }
                )
            }
            .toSingle(true)
    }

    override fun performFirstTimeSetup(): Observable<UpdateResult> {
        return Observable.empty()
    }

    override fun performUpdate(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMap { getFileFromFirebase(getStorageReference(it, FILE_NAME), FILE_NAME) }
            .observeOn(Schedulers.io())
            .flatMap { parseJsonFile<FirebaseCategoryResult>(it, FirebaseCategoryResult::class.java) }
            .flatMapCompletable { updateCategoriesDatabase(it) }
    }

    private fun updateCategoriesDatabase(result: FirebaseCategoryResult): Completable {
        return Completable.fromCallable {
            val categories = GwentCategoryMapper.mapToCategoryEntityList(result)
            database.categoryDao().insert(categories)
        }
    }
}
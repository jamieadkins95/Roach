package com.jamieadkins.gwent.data.update.repository

import android.content.res.AssetManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.data.card.mapper.ApiMapper
import com.jamieadkins.gwent.data.card.mapper.ArtApiMapper
import com.jamieadkins.gwent.data.card.model.FirebaseCardResult
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named

class CardUpdateRepository @Inject constructor(
    private val database: GwentDatabase,
    @Named("files") filesDirectory: File,
    private val patchRepository: PatchRepository,
    private val cardMapper: ApiMapper,
    private val artMapper: ArtApiMapper,
    preferences: RxSharedPreferences,
    private val assetManager: AssetManager
) : BaseUpdateRepository(filesDirectory, preferences) {

    override val FILE_NAME = "cards.json"

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
                gson.fromJson<FirebaseCardResult>(reader, FirebaseCardResult::class.java)
            }
        }.flatMapCompletable(::updateCardDatabase)
    }

    override fun internalPerformUpdate(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMap { getFileFromFirebase(getStorageReference(it, FILE_NAME), FILE_NAME) }
            .observeOn(Schedulers.io())
            .flatMap { parseJsonFile<FirebaseCardResult>(it, FirebaseCardResult::class.java) }
            .flatMapCompletable { updateCardDatabase(it) }
            .andThen(updateLastUpdated())
    }

    private fun updateCardDatabase(cardList: FirebaseCardResult): Completable {
        return Completable.fromCallable {
            val cards = cardMapper.map(cardList)
            database.cardDao().insertCards(cards)
            database.artDao().insertArt(artMapper.map(cardList))
        }
    }

    private fun updateLastUpdated(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMapCompletable { updateLocalLastUpdated(it, FILE_NAME) }
    }
}
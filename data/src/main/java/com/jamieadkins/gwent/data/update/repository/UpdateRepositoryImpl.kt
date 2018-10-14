package com.jamieadkins.gwent.data.update.repository

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.data.update.CardUpdate
import com.jamieadkins.gwent.data.update.CategoryUpdate
import com.jamieadkins.gwent.data.update.KeywordUpdate
import com.jamieadkins.gwent.data.update.NotificationsUpdate
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function4
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class UpdateRepositoryImpl @Inject constructor(
    private val database: GwentDatabase,
    @Named("files") filesDirectory: File,
    preferences: RxSharedPreferences,
    @NotificationsUpdate private val notificationsRepository: UpdateRepository,
    @CardUpdate private val cardUpdateRepository: UpdateRepository,
    @KeywordUpdate private val keywordUpdateRepository: UpdateRepository,
    @CategoryUpdate private val categoryUpdateRepository: UpdateRepository
) : BaseUpdateRepository(filesDirectory, preferences) {

    // Unused.
    override val FILE_NAME = ""

    override fun isUpdateAvailable(): Observable<Boolean> {
        return Observable.combineLatest(
            database.cardDao().count().toObservable(),
            cardUpdateRepository.isUpdateAvailable(),
            keywordUpdateRepository.isUpdateAvailable(),
            categoryUpdateRepository.isUpdateAvailable(),
            Function4 { cardsInDb: Int, card: Boolean, keyword: Boolean, category: Boolean ->
                cardsInDb == 0 || card || keyword || category
            })
            .distinctUntilChanged()
    }

    override fun internalPerformUpdate(): Completable {
        return cardUpdateRepository.performUpdate()
            .andThen(keywordUpdateRepository.performUpdate())
            .andThen(categoryUpdateRepository.performUpdate())
    }

    override fun hasDoneFirstTimeSetup(): Observable<Boolean> {
        return Observable.combineLatest(
            notificationsRepository.hasDoneFirstTimeSetup(),
            cardUpdateRepository.hasDoneFirstTimeSetup(),
            keywordUpdateRepository.hasDoneFirstTimeSetup(),
            categoryUpdateRepository.hasDoneFirstTimeSetup(),
            Function4 { notifications: Boolean, card: Boolean, keyword: Boolean, category: Boolean ->
                notifications && card && keyword && category
            })
            .distinctUntilChanged()
    }

    override fun internalPerformFirstTimeSetup(): Completable {
        return notificationsRepository.performFirstTimeSetup()
            .andThen(cardUpdateRepository.performFirstTimeSetup())
            .andThen(keywordUpdateRepository.performFirstTimeSetup())
            .andThen(categoryUpdateRepository.performFirstTimeSetup())
    }
}
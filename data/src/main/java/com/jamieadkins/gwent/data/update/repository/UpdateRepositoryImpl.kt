package com.jamieadkins.gwent.data.update.repository

import android.content.res.Resources
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.jamieadkins.gwent.data.BuildConfig
import com.jamieadkins.gwent.data.R
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import com.jamieadkins.gwent.domain.update.model.UpdateResult
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class UpdateRepositoryImpl(private val database: GwentDatabase,
                           filesDirectory: File,
                           private val preferences: RxSharedPreferences,
                           private val resources: Resources,
                           private val cardUpdateRepository: UpdateRepository,
                           private val keywordUpdateRepository: UpdateRepository,
                           private val categoryUpdateRepository: UpdateRepository,
                           private val patchRepository: PatchRepository) : BaseUpdateRepository(filesDirectory) {

    override fun isUpdateAvailable(): Single<Boolean> {
        return Single.zip(cardUpdateRepository.isUpdateAvailable(),
                          keywordUpdateRepository.isUpdateAvailable(),
                          categoryUpdateRepository.isUpdateAvailable(),
                          Function3 { card: Boolean, keyword: Boolean, category: Boolean ->
                              card || keyword || category
                          })
    }

    override fun performFirstTimeSetup(): Observable<UpdateResult> {
        val setup = preferences.getBoolean("setup", false)
            .asObservable()
            .flatMap { done ->
                if (done) {
                    performFirstTimeNotificationSetup()
                } else {
                    Observable.empty()
                }
            }
            .doOnComplete {
                preferences.getBoolean("setup").set(true)
            }

        return Observable.concat(setup,
                                 cardUpdateRepository.performFirstTimeSetup(),
                                 keywordUpdateRepository.performFirstTimeSetup(),
                                 categoryUpdateRepository.performFirstTimeSetup())
    }

    override fun internalPerformUpdate(): Completable {
        return cardUpdateRepository.performUpdate()
            .andThen(keywordUpdateRepository.performUpdate())
            .andThen(categoryUpdateRepository.performUpdate())
            .andThen(performPatchDatabaseUpdate())
    }

    private fun performFirstTimeNotificationSetup(): Observable<UpdateResult> {
        return Observable.fromCallable {
            val language = Locale.getDefault().language
            val newsLanguage = resources.getStringArray(R.array.locales_news).firstOrNull { it.contains(language) } ?: "en"
            preferences.getString(resources.getString(R.string.pref_news_notifications_key)).set(newsLanguage)
            preferences.getBoolean(resources.getString(R.string.shown_news)).set(true)
            setupNewsNotifications(resources, newsLanguage)
            UpdateResult.NotificationsSetup
        }
    }

    private fun setupNewsNotifications(resources: Resources, locale: String = "none") {
        unsubscribeFromAllNews(resources)

        if (locale == "none") {
            return
        }

        val topic = "news-$locale"
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic("$topic-debug")
        }
    }

    private fun unsubscribeFromAllNews(resources: Resources) {
        for (key in resources.getStringArray(R.array.locales_news)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news-$key")
            if (BuildConfig.DEBUG) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news-$key-debug")
            }
        }
    }

    private fun getLastUpdated(patch: String): Single<Long> {
        return getMetaData(getStorageReference(patch))
            .map { it.updatedTimeMillis }
    }

    private fun performPatchDatabaseUpdate(): Completable {
        return patchRepository.getLatestPatchId()
            .flatMap { patch ->
                getLastUpdated(patch)
                    .map { PatchVersionEntity(patch, patch, it) }
            }
            .observeOn(Schedulers.io())
            .flatMapCompletable { updatePatchDatabase(it) }
    }

    private fun updatePatchDatabase(newPatch: PatchVersionEntity): Completable {
        return Completable.fromCallable {
            database.patchDao().insertPatchVersion(newPatch)
        }
    }
}
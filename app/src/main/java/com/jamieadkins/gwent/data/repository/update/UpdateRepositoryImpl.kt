package com.jamieadkins.gwent.data.repository.update

import android.content.SharedPreferences
import android.content.res.Resources
import com.crashlytics.android.Crashlytics
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.Constants
import com.jamieadkins.gwent.StoreManager
import com.jamieadkins.gwent.data.card.CardsApi
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.data.repository.FirebasePatchResult
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import com.jamieadkins.gwent.main.GwentApplication
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import com.google.firebase.storage.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jamieadkins.gwent.domain.update.UpdateResult
import com.jamieadkins.gwent.data.keyword.FirebaseCategoryResult
import com.jamieadkins.gwent.data.keyword.FirebaseKeywordResult
import com.jamieadkins.gwent.data.repository.card.GwentCardMapper
import com.jamieadkins.gwent.database.GwentDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Type
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.jamieadkins.gwent.R
import java.util.*


class UpdateRepositoryImpl(private val database: GwentDatabase,
                           sharedPreferences: SharedPreferences,
                           private val resources: Resources) : UpdateRepository {

    private val preferences = RxSharedPreferences.create(sharedPreferences)

    private companion object {
        const val CARD_FILE_NAME = "cards.json"
        const val CATEGORIES_FILE_NAME = "categories.json"
        const val KEYWORDS_FILE_NAME = "keywords.json"
    }

    private val storage = FirebaseStorage.getInstance()
    val gson = Gson()
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build()
            .create(CardsApi::class.java)

    override fun isUpdateAvailable(): Single<Boolean> {
        return getLatestPatch()
                .map {
                    val storedPatch = getCachedPatch(it.patch)
                    storedPatch == null || it.patch != storedPatch.patch || it.lastUpdated > storedPatch.lastUpdated
                }
                .doOnSuccess { update -> if (update) log("Update Available.") else log("Up to date.") }
    }

    private fun getLatestPatch(): Single<PatchVersionEntity> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        return StoreManager.getDataOnce<FirebasePatchResult>(barcode, cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), FirebasePatchResult::class.java, 10)
                .flatMap { patch ->
                    getLastUpdated(patch.patch).map { Pair(patch, it) }
                }
                .map { PatchVersionEntity(it.first.patch, it.first.name, it.second) }
                .doOnSuccess { log("Latest Patch: " + it.patch + " " + it.lastUpdated) }
                .observeOn(Schedulers.io())
    }

    private fun getLastUpdated(patch: String): Single<Long> {
        return getMetaData(getStorageReference(patch, CARD_FILE_NAME))
                .map { it.updatedTimeMillis }
    }

    private fun getCachedPatch(patch: String): PatchVersionEntity? {
        val cached = database.patchDao().getPatchVersion(patch)
        log("Database Patch: " + cached?.patch + " " + cached?.lastUpdated)
        return cached
    }

    override fun performFirstTimeSetup(): Observable<UpdateResult> {
        return preferences.getBoolean("setup", false)
                .asObservable()
                .flatMap { done ->
                    if (done) {
                        performFirstTimeLanguageSetup()
                                .concatWith { performFirstTimeNotificationSetup() }
                    } else {
                        Observable.empty()
                    }
                }
                .doOnComplete {
                    preferences.getBoolean("setup").set(true)
                }
    }

    private fun performFirstTimeLanguageSetup(): Observable<UpdateResult> {
        return Observable.fromCallable {
            val language = Locale.getDefault().language
            val cardLanguage = resources.getStringArray(R.array.locales).firstOrNull { it.contains(language) } ?: "en-US"
            preferences.getString(resources.getString(R.string.pref_locale_key)).set(cardLanguage)
            preferences.getBoolean(resources.getString(R.string.shown_language)).set(true)
            UpdateResult.LanguageSetup
        }
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

    override fun performUpdate(): Completable {
        return performCardDatabaseUpdate()
                .andThen(performKeywordsUpdate())
                .andThen(performCategoriesUpdate())
                .andThen(performPatchDatabaseUpdate())
    }

    private fun performCardDatabaseUpdate(): Completable {
        return getLatestPatch()
                .flatMap { getFileFromFirebase(getStorageReference(it.patch, CARD_FILE_NAME), CARD_FILE_NAME) }
                .observeOn(Schedulers.io())
                .flatMap { parseJsonFile<FirebaseCardResult>(it, FirebaseCardResult::class.java) }
                .flatMapCompletable { updateCardDatabase(it) }
    }

    private fun updateCardDatabase(cardList: FirebaseCardResult): Completable {
        return Completable.fromCallable {
            val cards = GwentCardMapper.cardEntityListFromApiResult(cardList)
            log("Inserting " + cards.size + " cards into database.")
            database.cardDao().insertCards(cards)
            database.cardDao().insertArt(GwentCardMapper.artEntityListFromApiResult(cardList))
        }
    }

    private fun performCategoriesUpdate(): Completable {
        return getLatestPatch()
                .flatMap { getFileFromFirebase(getStorageReference(it.patch, CATEGORIES_FILE_NAME), CATEGORIES_FILE_NAME) }
                .observeOn(Schedulers.io())
                .flatMap { parseJsonFile<FirebaseCategoryResult>(it, FirebaseCategoryResult::class.java) }
                .flatMapCompletable { updateCategoriesDatabase(it) }
    }

    private fun updateCategoriesDatabase(result: FirebaseCategoryResult): Completable {
        return Completable.fromCallable {
            val categories = GwentCategoryMapper.mapToCategoryEntityList(result)
            log("Inserting ${categories.size} categories into database.")
            database.categoryDao().insert(categories)
        }
    }

    private fun performKeywordsUpdate(): Completable {
        return getLatestPatch()
                .flatMap { getFileFromFirebase(getStorageReference(it.patch, KEYWORDS_FILE_NAME), KEYWORDS_FILE_NAME) }
                .observeOn(Schedulers.io())
                .flatMap { parseJsonFile<FirebaseKeywordResult>(it, FirebaseKeywordResult::class.java) }
                .flatMapCompletable { updateKeywordDatabase(it) }
    }

    private fun updateKeywordDatabase(result: FirebaseKeywordResult): Completable {
        return Completable.fromCallable {
            val keywords = GwentKeywordMapper.mapToKeywordEntityList(result)
            log("Inserting ${keywords.size} keywords into database.")
            database.keywordDao().insert(keywords)
        }
    }

    private fun performPatchDatabaseUpdate(): Completable {
        return getLatestPatch()
                .flatMapCompletable { updatePatchDatabase(it) }
    }

    private fun updatePatchDatabase(newPatch: PatchVersionEntity): Completable {
        return Completable.fromCallable {
            log("Inserting patch " + newPatch.name + " into database.")
            database.patchDao().insertPatchVersion(newPatch)
        }
    }

    override fun getNewCardData(): Single<File> {
        return getLatestPatch()
                .flatMap { getFileFromFirebase(getStorageReference(it.patch, CARD_FILE_NAME), CARD_FILE_NAME) }
    }

    private fun getFileFromFirebase(storageRef: StorageReference, outputFileName: String): Single<File> {
        return Single.create<File> { emitter ->
            val file = File(GwentApplication.INSTANCE.filesDir, outputFileName)
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

    private fun getMetaData(storageRef: StorageReference): Single<StorageMetadata> {
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

    private fun getStorageReference(patch: String, fileName: String): StorageReference {
        return storage.reference.child("card-data").child(patch).child(fileName)
    }

    private fun <T> parseJsonFile(file: File, type: Type = genericType<T>()): Single<T> {
        return Single.fromCallable {
            log("Parsing JSON from ${file.absolutePath}")
            val bufferedReader = BufferedReader(FileReader(file))
            gson.fromJson<T>(bufferedReader, type)
        }
    }

    fun <T> genericType() = object : TypeToken<T>() {}.type

    private fun log(message: String) {
        Crashlytics.log(message)
        Timber.d(message)
    }
}
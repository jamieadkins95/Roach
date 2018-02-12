package com.jamieadkins.gwent.data.card

import android.support.v7.preference.PreferenceManager
import android.util.Log
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SearchEvent
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.Constants
import com.jamieadkins.gwent.StoreManager
import com.jamieadkins.gwent.StoreManager.provideGson
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.*
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.data.repository.FirebasePatchResult
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.model.CardArt
import com.jamieadkins.gwent.model.GwentCard
import com.nytimes.android.external.cache3.Cache
import com.nytimes.android.external.fs3.SourcePersisterFactory
import com.nytimes.android.external.store3.base.impl.BarCode
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CardsInteractorImpl : CardsInteractor {

    private val database = GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext)
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(StoreManager.provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)  // Fail early: check Retrofit configuration at creation time in Debug build.
            .build()
            .create(CardsApi::class.java)

    private fun getLatestPatch(): Single<FirebasePatchResult> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        return StoreManager.getDataOnce(barcode, cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), FirebasePatchResult::class.java, 10)
    }

    private fun getCachedPatch(patch: String): Int {
        val version = PreferenceManager.getDefaultSharedPreferences(GwentApplication.INSTANCE.applicationContext).getInt(patch, 0)
        return version
    }

    private fun getCardsFromApi(patch: String): Single<FirebaseCardResult> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch))
        return StoreManager.getDataOnce(barcode, cardsApi.fetchCards(patch), FirebaseCardResult::class.java, 10)
    }

    private fun updateCachedPatch(patch: String, version: Int): Completable {
        return Completable.fromCallable {
            PreferenceManager.getDefaultSharedPreferences(GwentApplication.INSTANCE.applicationContext).edit().putInt(patch, version).commit()
        }
    }

    private fun updateDatabase(cardList: FirebaseCardResult): Completable {
        return Completable.fromCallable {
            database.cardDao().insertCards(cardEntityListFromApiResult(cardList))
            database.cardDao().insertArt(artEntityListFromApiResult(cardList))
        }
    }

    override fun getAllCards(): Flowable<Collection<GwentCard>> {
        return database.cardDao().subscribeToCards()
                .flatMapSingle { cards ->
                    database.cardDao().getCardArt().map { artList ->
                        val artMap = mutableMapOf<String, MutableList<ArtEntity>>()
                        artList.forEach {
                            if (artMap[it.cardId] == null) {
                                artMap[it.cardId] = mutableListOf()
                            }
                            artMap[it.cardId]?.add(it)
                        }
                        cards.forEach {
                            it.art = artMap[it.id]
                        }
                        cards
                    }
                }
                .map { gwentCardListFromCardEntityList(it) }
    }

    override fun getCards(filter: CardFilter): Flowable<Collection<GwentCard>> {
        return getCards(filter, null, null)
    }

    override fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<Collection<GwentCard>> {
        return getCards(filter, null, cardIds)
    }

    override fun getCards(filter: CardFilter?, query: String?): Flowable<Collection<GwentCard>> {
        return getCards(filter, query, null)
    }

    private fun getCards(filter: CardFilter?, query: String?, cardIds: List<String>?): Flowable<Collection<GwentCard>> {
        var source: Flowable<Collection<GwentCard>> = getAllCards()

        if (query != null) {
            source = getAllCards().flatMap { cardList ->
                val searchResults = CardSearch.searchCards(query, cardList.toList())
                Answers.getInstance().logSearch(SearchEvent()
                        .putQuery(query)
                        .putCustomAttribute("hits", searchResults.size))
                getAllCards().map { it.filter { searchResults.contains(it.id) } }
            }
        } else if (cardIds != null) {
            source = getAllCards().map { it.filter { cardIds.contains(it.id) } }
        }

        filter?.let { cardFilter ->
            source = source.map { it.filter { cardFilter.doesCardMeetFilter(it) } }
        }

        return source
    }

    override fun getCard(id: String): Flowable<GwentCard> {
        return getLatestPatch()
                .flatMapObservable { patch ->
                    val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch.patch, id))
                    StoreManager.getData<CardDetails>(barcode, cardsApi.fetchCard(patch.patch, id), CardDetails::class.java, 10).toObservable()
                }
                .map { Mapper.cardDetailsToGwentCard(it) }
                .toFlowable(BackpressureStrategy.LATEST)
    }

    private fun cardEntityListFromApiResult(result: FirebaseCardResult): Collection<CardEntity> {
        val cardList = mutableListOf<CardEntity>()
        result.values.forEach {
            if (it.isReleased) {
                val variation = it.variations.values.firstOrNull()
                cardList.add(
                        CardEntity(
                                it.ingameId,
                                it.strength,
                                variation?.isCollectible ?: false,
                                it.rarity ?: "",
                                it.type ?: "",
                                it.faction ?: "",
                                it.name ?: mapOf(),
                                it.info ?: mapOf(),
                                it.flavor ?: mapOf(),
                                it.categories ?: listOf(),
                                it.loyalties ?: listOf(),
                                it.related ?: listOf()
                        )
                )
            }
        }
        return cardList
    }

    private fun artEntityListFromApiResult(result: FirebaseCardResult): Collection<ArtEntity> {
        val artList = mutableListOf<ArtEntity>()
        result.values.forEach { card ->
            if (card.isReleased) {
                val variation = card.variations.values.firstOrNull()
                variation?.let { variation ->
                    artList.add(
                            ArtEntity(
                                    variation.variationId,
                                    card.ingameId,
                                    variation.art.original,
                                    variation.art.high,
                                    variation.art.medium,
                                    variation.art.low,
                                    variation.art.thumbnail
                            )
                    )
                }
            }
        }
        return artList
    }

    private fun gwentCardListFromCardEntityList(entityList: Collection<CardEntity>): Collection<GwentCard> {
        val cardList = mutableListOf<GwentCard>()
        entityList.forEach {
            val card =  GwentCard()
            card.id = it.id
            card.name = it.name
            card.info = it.tooltip
            card.flavor = it.flavor
            card.strength = it.strength
            card.collectible = it.collectible

            card.faction = Mapper.factionIdToFaction(it.faction)
            card.colour = Mapper.typeToColour(it.color)
            card.rarity = Mapper.rarityIdToRarity(it.rarity)
            card.cardArt = it.art?.firstOrNull()?.let { artEntityToCardArt(it) }
            cardList.add(card)
        }
        return cardList
    }

    private fun artEntityToCardArt(artEntity: ArtEntity): CardArt {
        val art = CardArt()
        art.original = artEntity.original
        art.high = artEntity.high
        art.medium = artEntity.medium
        art.low= artEntity.low
        art.thumbnail = artEntity.thumbnail
        return art
    }
}
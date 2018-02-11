package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.Constants
import com.jamieadkins.gwent.StoreManager
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardSearch
import com.jamieadkins.gwent.data.card.CardsApi
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.jamieadkins.gwent.data.repository.FirebasePatchResult
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.database.entity.PatchVersionEntity
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.model.GwentCard
import com.jamieadkins.gwent.model.patch.PatchState
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CardRepositoryImpl : CardRepository {

    private val database = GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext)
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(StoreManager.provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build()
            .create(CardsApi::class.java)

    override fun checkForUpdates(): Single<PatchState> {
        return getLatestPatch()
                .map { newPatch ->
                    val storedPatch = getCachedPatch(newPatch.patch)
                    when {
                        newPatch.patch != storedPatch.patch -> PatchState.NewPatch(newPatch.name)
                        newPatch.version > storedPatch.version -> PatchState.NewVersion(newPatch.name, newPatch.version)
                        else -> PatchState.NoUpdate()
                    }
                }
    }

    override fun performUpdate(patch: String): Completable {
        return getLatestPatch()
                .flatMap { latestPatch -> getCardsFromApi(latestPatch.patch).map { Pair(latestPatch, it) } }
                .flatMapCompletable {
                    updateDisk(PatchVersionEntity(it.first.patch, it.first.version), it.second)
                }
    }

    private fun getLatestPatch(): Single<FirebasePatchResult> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        return StoreManager.getDataOnce(barcode, cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), FirebasePatchResult::class.java, 10)
    }

    private fun getCachedPatch(patch: String): PatchVersionEntity {
        val cached = database.patchDao().getPatchVersion(patch)
        return cached
    }

    private fun getCardsFromApi(patch: String): Single<FirebaseCardResult> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch))
        return StoreManager.getDataOnce(barcode, cardsApi.fetchCards(patch), FirebaseCardResult::class.java, 10)
    }

    private fun updateDisk(newPatch: PatchVersionEntity, cardList: FirebaseCardResult): Completable {
        return Completable.fromCallable {
            database.cardDao().insertCards(CardMapper.cardEntityListFromApiResult(cardList))
            database.patchDao().insertPatchVersion(newPatch)
        }

    }

    private fun getAllCards(): Single<Collection<GwentCard>> {
        return database.cardDao().getCards()
                .map { CardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun getCards(cardFilter: CardFilter?): Single<Collection<GwentCard>> {
        return if (cardFilter == null) {
            getAllCards()
        } else {
            getAllCards().map { it.filter { cardFilter.doesCardMeetFilter(it) } }
        }
    }

    override fun getCards(cardIds: List<String>): Single<Collection<GwentCard>> {
        return database.cardDao().getCards(cardIds).map { CardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun searchCards(query: String): Single<Collection<GwentCard>> {
        return getAllCards().flatMap { cardList ->
            val searchResults = CardSearch.searchCards(query, cardList.toList())
            getCards(searchResults)
        }
    }

    override fun getCard(id: String): Single<GwentCard> {
        return database.cardDao().getCard(id)
                .map { CardMapper.cardEntityToGwentCard(it) }
    }
}
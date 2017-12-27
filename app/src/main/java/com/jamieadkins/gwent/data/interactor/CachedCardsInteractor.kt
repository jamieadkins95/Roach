package com.jamieadkins.gwent.data.interactor

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SearchEvent
import com.google.firebase.database.*
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.Constants
import com.jamieadkins.gwent.StoreManager
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.*
import com.jamieadkins.gwent.data.repository.FirebaseCardResult
import com.nytimes.android.external.store3.base.impl.BarCode
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap

class CachedCardsInteractor() : CardsInteractor {
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(StoreManager.provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)  // Fail early: check Retrofit configuration at creation time in Debug build.
            .build()
            .create(CardsApi::class.java)

    private fun getLatestPatch() : Flowable<String> {
        val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        return StoreManager.getData(barcode, cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), String::class.java, 10)
    }

    override fun getAllCards(): Flowable<Collection<CardDetails>> {
        return getLatestPatch()
                .flatMap { patch ->
                    val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch))
                    StoreManager.getData<FirebaseCardResult>(barcode, cardsApi.fetchCards(patch), FirebaseCardResult::class.java, 10)
                }
                .map { it.values }
    }

    override fun getCards(filter: CardFilter): Flowable<Collection<CardDetails>> {
        return getCards(filter, null, null)
    }

    override fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<Collection<CardDetails>> {
        return getCards(filter, null, cardIds)
    }

    override fun getCards(filter: CardFilter?, query: String?): Flowable<Collection<CardDetails>> {
        return getCards(filter, query, null)
    }

    private fun getCards(filter: CardFilter?, query: String?, cardIds: List<String>?): Flowable<Collection<CardDetails>> {
        var source: Flowable<Collection<CardDetails>> = getAllCards()

        if (query != null) {
            source = getAllCards().flatMap { cardList ->
                val searchResults = searchCards(query, cardList.toList(), "en-US")
                Answers.getInstance().logSearch(SearchEvent()
                        .putQuery(query)
                        .putCustomAttribute("hits", searchResults.size))
                getAllCards().map { it.filter { searchResults.contains(it.ingameId) } }
            }
        } else if (cardIds != null) {
            source = getAllCards().map { it.filter { cardIds.contains(it.ingameId) } }
        }

        filter?.let { cardFilter ->
            source = source.map { it.filter { cardFilter.doesCardMeetFilter(it) } }
        }

        return source
    }

    override fun getCard(id: String): Flowable<CardDetails> {
        return getLatestPatch()
                .flatMap { patch ->
                    val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch, id))
                    StoreManager.getData<CardDetails>(barcode, cardsApi.fetchCard(patch, id), CardDetails::class.java, 10)
                }
    }
}
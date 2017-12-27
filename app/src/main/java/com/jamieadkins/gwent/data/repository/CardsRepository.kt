package com.jamieadkins.gwent.data.repository

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.SearchEvent
import com.jamieadkins.gwent.data.CardDetails
import io.reactivex.Flowable
import com.jamieadkins.gwent.BuildConfig
import com.jamieadkins.gwent.Constants
import com.jamieadkins.gwent.StoreManager
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardsApi
import com.jamieadkins.gwent.data.searchCards
import com.nytimes.android.external.store3.base.impl.BarCode
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class CardsRepository() : CardsDataSource {
    private val cardsApi = Retrofit.Builder()
            .baseUrl(Constants.CARDS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(StoreManager.provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)  // Fail early: check Retrofit configuration at creation time in Debug build.
            .build()
            .create(CardsApi::class.java)

    override fun getAllCards(): Flowable<List<CardDetails>> {
        val patchBarCode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("patch", BuildConfig.CARD_DATA_VERSION))
        val patchSource = StoreManager.getData<String>(patchBarCode,
                cardsApi.fetchPatch(BuildConfig.CARD_DATA_VERSION), 10)

        return patchSource
                .flatMap { patch ->
                    val barcode = BarCode(Constants.CACHE_KEY, StoreManager.generateId("card-data", patch))
                    StoreManager.getData<Map<String, CardDetails>>(barcode, cardsApi.fetchCards(patch), 10)
                }
                .map { it.values.toList() }
    }

    override fun getCards(filter: CardFilter): Flowable<List<CardDetails>> {
        return getCards(filter, null, null)
    }

    override fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<List<CardDetails>> {
        return getCards(filter, null, cardIds)
    }

    override fun getCards(filter: CardFilter?, query: String?): Flowable<List<CardDetails>> {
        return getCards(filter, query, null)
    }

    private fun getCards(filter: CardFilter?, query: String?, cardIds: List<String>?): Flowable<List<CardDetails>> {
        var source: Flowable<List<CardDetails>> = getAllCards()

        if (query != null) {
            source = getAllCards().flatMap { cardList ->
                val searchResults = searchCards(query, cardList, "en-US")
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
        return Flowable.empty()
    }

}
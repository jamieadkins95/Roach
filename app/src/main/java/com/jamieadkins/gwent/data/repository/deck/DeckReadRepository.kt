package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.model.GwentDeck
import com.jamieadkins.gwent.model.GwentDeckSummary
import com.jamieadkins.gwent.model.GwentDeckCardCounts
import io.reactivex.Flowable

import io.reactivex.Single

interface DeckReadRepository {

    fun getDecks(): Single<List<GwentDeckSummary>>

    fun getDeck(deckId: String): Single<GwentDeck>

    fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary>

    fun getDeckCardCounts(deckId: String): Flowable<GwentDeckCardCounts>
}

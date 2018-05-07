package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.model.deck.GwentDeck
import com.jamieadkins.gwent.model.deck.GwentDeckSummary
import com.jamieadkins.gwent.model.deck.GwentDeckCardCounts
import io.reactivex.Flowable

import io.reactivex.Single

interface DeckReadRepository {

    fun getDecks(): Single<List<GwentDeckSummary>>

    fun getDeckOnce(deckId: String): Single<GwentDeck>

    fun getDeckFaction(deckId: String): Single<GwentFaction>

    fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary>

    fun getDeckCardCounts(deckId: String): Flowable<GwentDeckCardCounts>
}

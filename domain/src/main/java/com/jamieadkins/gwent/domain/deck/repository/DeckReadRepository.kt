package com.jamieadkins.gwent.domain.deck.repository

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.model.GwentDeckSummary
import com.jamieadkins.gwent.domain.deck.model.GwentDeckCardCounts
import io.reactivex.Flowable

import io.reactivex.Single

interface DeckReadRepository {

    fun getDecks(): Single<List<GwentDeckSummary>>

    fun getDeckOnce(deckId: String): Single<GwentDeck>

    fun getDeckFaction(deckId: String): Single<GwentFaction>

    fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary>

    fun getDeckCardCounts(deckId: String): Flowable<GwentDeckCardCounts>
}

package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.model.GwentDeck

import io.reactivex.Single

interface DeckReadRepository {

    fun getDecks(): Single<Collection<GwentDeck>>

    fun getDeck(deckId: String): Single<GwentDeck>
}

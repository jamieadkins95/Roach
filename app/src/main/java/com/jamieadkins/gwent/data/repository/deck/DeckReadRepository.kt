package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.data.deck.Deck
import com.jamieadkins.gwent.model.GwentFaction

import io.reactivex.Flowable
import io.reactivex.Single

interface DeckReadRepository {

    fun getDecks(): Single<Collection<Deck>>

    fun getDeck(deckId: String): Single<Deck>
}

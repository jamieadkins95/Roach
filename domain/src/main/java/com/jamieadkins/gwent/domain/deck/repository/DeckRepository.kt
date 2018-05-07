package com.jamieadkins.gwent.domain.deck.repository

import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.GwentFaction
import io.reactivex.Completable

import io.reactivex.Flowable
import io.reactivex.Single

interface DeckRepository : DeckReadRepository {

    fun getDeck(deckId: String): Flowable<GwentDeck>

    fun createNewDeck(name: String, faction: GwentFaction): Single<String>

    fun addCardToDeck(deckId: String, cardId: String): Completable

    fun setLeader(deckId: String, leaderId: String): Completable

    fun renameDeck(deckId: String, newName: String): Completable

    fun removeCardFromDeck(deckId: String, cardId: String): Completable

    fun deleteDeck(deckId: String): Completable
}

package com.jamieadkins.gwent.domain.deck.repository

import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.GwentFaction
import io.reactivex.Completable

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface DeckRepository {

    fun getDecks(): Observable<List<GwentDeck>>

    fun getDeckOnce(deckId: String): Single<GwentDeck>

    fun getDeck(deckId: String): Observable<GwentDeck>

    fun createNewDeck(name: String, faction: GwentFaction): Single<String>

    fun updateCardCount(deckId: String, cardId: String, count: Int): Completable

    fun setLeader(deckId: String, leaderId: String): Completable

    fun renameDeck(deckId: String, newName: String): Completable

    fun deleteDeck(deckId: String): Completable

    fun getDeckFaction(deckId: String): Single<GwentFaction>
}

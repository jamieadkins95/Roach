package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.model.GwentDeck
import com.jamieadkins.gwent.model.GwentFaction
import io.reactivex.Completable

import io.reactivex.Flowable
import io.reactivex.Single

interface DeckRepository : DeckReadRepository {

    fun getDeckUpdates(deckId: String): Flowable<GwentDeck>

    fun createNewDeck(name: String, faction: GwentFaction): Single<String>

    fun addCardToDeck(deckId: String, cardId: String): Completable

    fun setLeader(deckId: String, leaderId: String): Completable

    fun renameDeck(deckId: String, newName: String): Completable

    fun removeCardFromDeck(deckId: String, cardId: String): Completable

    fun deleteDeck(deckId: String): Completable
}

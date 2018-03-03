package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.data.deck.Deck
import com.jamieadkins.gwent.model.GwentFaction

import io.reactivex.Flowable
import io.reactivex.Single

interface DeckRepository : DeckReadRepository {

    fun getDeckUpdates(deckId: String): Flowable<Deck>

    fun createNewDeck(name: String, faction: GwentFaction): String

    fun addCardToDeck(deckId: String, cardId: String)

    fun setLeader(deckId: String, leaderId: String)

    fun renameDeck(deckId: String, newName: String)

    fun removeCardFromDeck(deckId: String, cardId: String)

    fun deleteDeck(deckId: String)
}

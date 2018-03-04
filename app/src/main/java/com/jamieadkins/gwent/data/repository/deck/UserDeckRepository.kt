package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.repository.card.CardMapper
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentDeck
import com.jamieadkins.gwent.model.GwentDeckSummary
import com.jamieadkins.gwent.model.GwentFaction
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserDeckRepository(private val database: GwentDatabase) : DeckRepository {

    override fun getDecks(): Single<Collection<GwentDeck>> {
        return database.deckDao().getDecks()
                .map { DeckMapper.deckEntityListToGwentDeckList(it) }
    }

    override fun getDeckUpdates(deckId: String): Flowable<GwentDeck> {
        return database.deckDao().getDeckUpdates(deckId)
                .map { DeckMapper.deckEntityToGwentDeck(it) }
    }

    override fun getDeck(deckId: String): Single<GwentDeck> {
        return database.deckDao().getDeck(deckId)
                .map { DeckMapper.deckEntityToGwentDeck(it) }
    }

    override fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary> {
        return database.deckCardDao().getCardCounts(deckId)
                // Get all card ids.
                .map { it.map { it.cardId } }
                // Get all card entities.
                .flatMapSingle { database.cardDao().getCards(it) }
                // Map entities to GwentCards.
                .map { CardMapper.gwentCardListFromCardEntityList(it) }
                // Map to GwentDeckSummary
                .map { cards ->
                    var bronzeCount = 0
                    var silverCount = 0
                    var goldCount = 0
                    cards.forEach {
                        when (it.colour) {
                            CardColour.BRONZE -> bronzeCount++
                            CardColour.SILVER -> silverCount++
                            CardColour.GOLD -> goldCount++
                        }
                    }
                    GwentDeckSummary(bronzeCount, silverCount, goldCount)
                }
    }

    override fun createNewDeck(name: String, faction: GwentFaction): Single<String> {
        return Single.fromCallable {
            val deck = DeckEntity(name, Mapper.factionToFactionId(faction))
            database.deckDao().insertDeck(deck).toString()
        }
    }

    override fun addCardToDeck(deckId: String, cardId: String): Completable {
        val defaultEntity = DeckCardEntity(deckId, cardId, 1)
        return database.deckCardDao().getCardCount(deckId, cardId)
                .switchIfEmpty(insert(defaultEntity)
                        .flatMapMaybe { database.deckCardDao().getCardCount(deckId, cardId) })
                .flatMapCompletable { updateCardCount(deckId, cardId, it.count)}
    }

    override fun setLeader(deckId: String, leaderId: String): Completable {
        return Completable.fromCallable {
            database.deckDao().changeDeckLeader(deckId, leaderId)
        }
    }

    override fun renameDeck(deckId: String, newName: String): Completable {
        return Completable.fromCallable {
            database.deckDao().changeDeckName(deckId, newName)
        }
    }

    override fun removeCardFromDeck(deckId: String, cardId: String): Completable {
        return database.deckCardDao().getCardCount(deckId, cardId)
                .flatMapCompletable {
                    if (it.count <= 1) {
                        removeCard(it.deckId, it.cardId)
                    } else {
                        updateCardCount(it.deckId, it.cardId, it.count)
                    }
                }
    }

    private fun insert(entity: DeckCardEntity): Single<Long> {
        return Single.fromCallable {
            database.deckCardDao().insert(entity)
        }
    }

    private fun removeCard(deckId: String, cardId: String): Completable {
        return Completable.fromCallable {
            database.deckCardDao().removeCard(deckId, cardId)
        }
    }

    private fun updateCardCount(deckId: String, cardId: String, count: Int): Completable {
        return Completable.fromCallable {
            database.deckCardDao().updateCardCount(deckId, cardId, count)
        }
    }

    override fun deleteDeck(deckId: String): Completable {
        return Completable.fromCallable {
            database.deckDao().deleteDeck(deckId)
        }
    }
}
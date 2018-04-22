package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.core.GwentCardColour
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.data.repository.card.GwentCardMapper
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.model.*
import com.jamieadkins.gwent.model.deck.GwentDeck
import com.jamieadkins.gwent.model.deck.GwentDeckCardCounts
import com.jamieadkins.gwent.model.deck.GwentDeckSummary
import io.reactivex.*
import io.reactivex.functions.BiFunction

class UserDeckRepository(private val database: GwentDatabase) : DeckRepository {

    override fun getDecks(): Single<List<GwentDeckSummary>> {
        return database.deckDao().getDecksOnce()
                .map { DeckMapper.deckEntityListToGwentDeckList(it).toList() }
                .flatMap {
                    Observable.fromIterable(it)
                            .flatMap { deck ->
                                getLeader(deck.id)
                                        .zipWith(getCardCounts(deck.id).toMaybe(),
                                                BiFunction { leader: GwentCard, cardCounts: GwentDeckCardCounts
                                                    -> GwentDeckSummary(deck, leader, cardCounts)
                                                })
                                        .toObservable()
                            }
                            .toList()
                }
    }

    override fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary> {
        return database.deckDao().getDeck(deckId)
                .map { DeckMapper.deckEntityToGwentDeck(it) }
                .flatMap {
                    getLeader(it.id)
                            .zipWith(getCardCounts(it.id).toMaybe(),
                                    BiFunction { leader: GwentCard, cardCounts: GwentDeckCardCounts
                                        -> GwentDeckSummary(it, leader, cardCounts)
                                    })
                            .toFlowable()
                }
    }

    private fun getLeader(deckId: String): Maybe<GwentCard> {
        return database.deckDao().getDeckOnce(deckId)
                .flatMapMaybe {
                    if (it.leaderId != null) {
                        database.cardDao().getCard(it.leaderId!!)
                                .map { GwentCardMapper.cardEntityToGwentCard(it) }
                                .toMaybe()
                    } else {
                        Maybe.empty<GwentCard>()
                    }
                }
    }

    private fun getCardCounts(deckId: String): Single<GwentDeckCardCounts> {
        return getDeckCardCounts(deckId)
                .first(GwentDeckCardCounts(0, 0, 0))
    }

    override fun getDeck(deckId: String): Flowable<GwentDeck> {
        return database.deckDao().getDeck(deckId)
                .map { DeckMapper.deckEntityToGwentDeck(it) }
    }

    override fun getDeckOnce(deckId: String): Single<GwentDeck> {
        return database.deckDao().getDeckOnce(deckId)
                .map { DeckMapper.deckEntityToGwentDeck(it) }
    }

    override fun getDeckFaction(deckId: String): Single<GwentFaction> {
        return database.deckDao().getDeckOnce(deckId)
                .map { GwentCardMapper.factionIdToFaction(it.factionId) }
    }

    override fun getDeckCardCounts(deckId: String): Flowable<GwentDeckCardCounts> {
        return database.deckCardDao().getCardCounts(deckId)
                // Get all card ids.
                .map { it.map { it.cardId } }
                // Get all card entities.
                .flatMapSingle { database.cardDao().getCards(it) }
                // Map entities to GwentCards.
                .map { GwentCardMapper.gwentCardListFromCardEntityList(it) }
                // Map to GwentDeckCardCounts
                .map { cards ->
                    var bronzeCount = 0
                    var silverCount = 0
                    var goldCount = 0
                    cards.forEach {
                        when (it.colour) {
                            GwentCardColour.BRONZE -> bronzeCount++
                            GwentCardColour.SILVER -> silverCount++
                            GwentCardColour.GOLD -> goldCount++
                        }
                    }
                    GwentDeckCardCounts(bronzeCount, silverCount, goldCount)
                }
    }

    override fun createNewDeck(name: String, faction: GwentFaction): Single<String> {
        return Single.fromCallable {
            val deck = DeckEntity(name, GwentCardMapper.factionToFactionId(faction))
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
package com.jamieadkins.gwent.data.deck.repository

import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.card.model.Faction
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.database.entity.DeckWithCardsEntity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

class UserDeckRepository @Inject constructor(
    private val database: GwentDatabase,
    private val factionMapper: FactionMapper,
    private val cardRepository: CardRepository) : DeckRepository {

    override fun getDecks(): Observable<List<GwentDeck>> {
        return database.deckDao().getDecks()
            .toObservable()
            .map {
                it.map {
                    val deck = DeckWithCardsEntity()
                    deck.deck = it.deck
                    deck.cards = it.cards.filter { it.count > 0 }
                    deck
                }
            }
            .switchMap { decks ->
                Observable.fromIterable(decks)
                    .flatMapSingle { deckWithCards ->
                        Single.zip(
                            cardRepository.getCard(deckWithCards.deck.leaderId).firstOrError(),
                            cardRepository.getCards(deckWithCards.cards.map { it.cardId }).firstOrError(),
                            BiFunction { leader: GwentCard, cards: List<GwentCard> ->
                                GwentDeck(
                                    deckWithCards.deck.id.toString(),
                                    deckWithCards.deck.name,
                                    factionMapper.map(deckWithCards.deck.factionId),
                                    leader,
                                    deckWithCards.deck.created,
                                    cards.map { Pair(it.id, it) }.toMap(),
                                    deckWithCards.cards
                                        .map { Pair(it.cardId, it.count) }
                                        .toMap()
                                )
                            })
                    }
                    .toList()
                    .toObservable()
            }
    }

    override fun getDeck(deckId: String): Observable<GwentDeck> {
        return database.deckDao().getDeck(deckId)
            .toObservable()
            .map {
                val deck = DeckWithCardsEntity()
                deck.deck = it.deck
                deck.cards = it.cards.filter { it.count > 0 }
                deck
            }
            .switchMap { deckWithCards ->
                Observable.combineLatest(
                    cardRepository.getCard(deckWithCards.deck.leaderId),
                    cardRepository.getCards(deckWithCards.cards.map { it.cardId }),
                    BiFunction { leader: GwentCard, cards: List<GwentCard> ->
                        GwentDeck(
                            deckWithCards.deck.id.toString(),
                            deckWithCards.deck.name,
                            factionMapper.map(deckWithCards.deck.factionId),
                            leader,
                            deckWithCards.deck.created,
                            cards.map { Pair(it.id, it) }.toMap(),
                            deckWithCards.cards
                                .map { Pair(it.cardId, it.count) }
                                .toMap()
                        )
                    })
            }
    }

    override fun getDeckOnce(deckId: String): Single<GwentDeck> {
        return getDeck(deckId).firstOrError()
    }

    override fun createNewDeck(name: String, faction: GwentFaction): Single<String> {
        return Single.fromCallable {
            val defaultLeaderId = when (faction) {
                GwentFaction.NORTHERN_REALMS -> "200168" // Foltest
                GwentFaction.MONSTER -> "131101" // Eredin
                GwentFaction.SKELLIGE -> "200160" // Crach
                GwentFaction.SCOIATAEL -> "201589" // Filavandrel
                GwentFaction.NILFGAARD -> "200162" // Emhyr
                else -> throw IllegalArgumentException("Unrecognised Faction $faction")
            }
            val deck = DeckEntity(name, factionToFactionId(faction), defaultLeaderId)
            database.deckDao().insertDeck(deck).toString()
        }
    }

    override fun updateCardCount(deckId: String, cardId: String, count: Int): Completable {
        return Completable.fromCallable {
            database.deckCardDao().insert(DeckCardEntity(deckId, cardId, count))
        }
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

    override fun deleteDeck(deckId: String): Completable {
        return Completable.fromCallable {
            database.deckDao().deleteDeck(deckId)
        }
    }

    private fun factionToFactionId(faction: GwentFaction): String {
        return when (faction) {
            GwentFaction.MONSTER -> Faction.MONSTERS_ID
            GwentFaction.NORTHERN_REALMS -> Faction.NORTHERN_REALMS_ID
            GwentFaction.SCOIATAEL -> Faction.SCOIATAEL_ID
            GwentFaction.SKELLIGE -> Faction.SKELLIGE_ID
            GwentFaction.NILFGAARD -> Faction.NILFGAARD_ID
            GwentFaction.NEUTRAL -> Faction.NEUTRAL_ID
            else -> throw Exception("Faction not found")
        }
    }
}
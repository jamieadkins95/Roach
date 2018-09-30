package com.jamieadkins.gwent.data.deck.repository

import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.card.mapper.GwentCardMapper
import com.jamieadkins.gwent.data.card.model.Faction
import com.jamieadkins.gwent.data.deck.mapper.DeckMapper
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.DeckCardEntity
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.model.GwentDeckCardCounts
import com.jamieadkins.gwent.domain.deck.model.GwentDeckSummary
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class UserDeckRepository @Inject constructor(
    private val database: GwentDatabase,
    private val cardMapper: GwentCardMapper,
    private val factionMapper: FactionMapper,
    private val deckMapper: DeckMapper,
    private val localeRepository: LocaleRepository) : DeckRepository {

    override fun getDecks(): Single<List<GwentDeckSummary>> {
        return database.deckDao().getDecksOnce()
            .map { deckMapper.mapList(it) }
            .flatMap {
                Observable.fromIterable(it)
                    .flatMap { deck ->
                        getLeader(deck.id)
                            .zipWith(getCardCounts(deck.id).toMaybe(),
                                     BiFunction { leader: GwentCard, cardCounts: GwentDeckCardCounts
                                         ->
                                         GwentDeckSummary(deck, leader, cardCounts)
                                     })
                            .toObservable()
                    }
                    .toList()
            }
    }

    override fun getDeckSummary(deckId: String): Flowable<GwentDeckSummary> {
        return database.deckDao().getDeck(deckId)
            .map { deckMapper.map(it) }
            .flatMap {
                getLeader(it.id)
                    .zipWith(getCardCounts(it.id).toMaybe(),
                             BiFunction { leader: GwentCard, cardCounts: GwentDeckCardCounts
                                 ->
                                 GwentDeckSummary(it, leader, cardCounts)
                             })
                    .toFlowable()
            }
    }

    private fun getLeader(deckId: String): Maybe<GwentCard> {
        return Single.zip(database.deckDao().getDeckOnce(deckId),
                          localeRepository.getLocale().firstOrError(),
                          BiFunction { deck: DeckEntity, locale: String -> Pair(deck, locale) })
            .flatMapMaybe { deckAndLocale ->
                val leaderId = deckAndLocale.first.leaderId
                if (leaderId != null) {
                    database.cardDao().getCard(leaderId)
                        .map { cardMapper.map(it, deckAndLocale.second) }
                        .firstElement()
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
            .map { deckMapper.map(it) }
    }

    override fun getDeckOnce(deckId: String): Single<GwentDeck> {
        return database.deckDao().getDeckOnce(deckId)
            .map { deckMapper.map(it) }
    }

    override fun getDeckFaction(deckId: String): Single<GwentFaction> {
        return database.deckDao().getDeckOnce(deckId)
            .map { factionMapper.map(it.factionId) }
    }

    override fun getDeckCardCounts(deckId: String): Flowable<GwentDeckCardCounts> {
        return database.deckCardDao().getCardCounts(deckId)
            // Get all card ids.
            .map { it.map { it.cardId } }
            // Get all card entities.
            .switchMap {
                Flowable.combineLatest(
                    database.cardDao().getCards(it),
                    localeRepository.getLocale().toFlowable(BackpressureStrategy.LATEST),
                    BiFunction { cards: List<CardWithArtEntity>, locale: String ->
                        cardMapper.mapList(cards, locale)
                    })
            }
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
            val deck = DeckEntity(name, factionToFactionId(faction))
            database.deckDao().insertDeck(deck).toString()
        }
    }

    override fun addCardToDeck(deckId: String, cardId: String): Completable {
        val defaultEntity = DeckCardEntity(deckId, cardId, 1)
        return database.deckCardDao().getCardCount(deckId, cardId)
            .switchIfEmpty(
                insert(defaultEntity)
                    .flatMapMaybe { database.deckCardDao().getCardCount(deckId, cardId) }
            )
            .flatMapCompletable { updateCardCount(deckId, cardId, it.count) }
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
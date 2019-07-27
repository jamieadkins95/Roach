package com.jamieadkins.gwent.data.filter.repository

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentCardType
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val deckRepository: DeckRepository
) : FilterRepository {

    private val filters = mutableMapOf<String, BehaviorSubject<CardFilter>>()

    override fun getFilter(deckId: String): Observable<CardFilter> {
        return if (deckId.isEmpty()) {
            filters.getOrPut(getKey(deckId)) { BehaviorSubject.createDefault(getDefaultCardFilter()) }
        } else {
            deckRepository.getDeckFaction(deckId)
                .flatMapObservable { faction ->
                    filters.getOrPut(getKey(deckId)) { BehaviorSubject.createDefault(getCardFilterForFaction(faction)) }
                }
        }
    }

    override fun setFilter(deckId: String, cardFilter: CardFilter) {
        filters[getKey(deckId)]?.onNext(cardFilter)
    }

    override fun resetFilter(deckId: String) {
        val filter = if (deckId.isEmpty()) {
            Single.just(getDefaultCardFilter())
        } else {
            deckRepository.getDeckFaction(deckId)
                .map { faction ->
                    getCardFilterForFaction(faction)
                }
        }

        filter
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeWith(object : SingleObserver<CardFilter> {
            override fun onSuccess(cardFilter: CardFilter) {
                filters[getKey(deckId)]?.onNext(cardFilter)
            }

            override fun onSubscribe(d: Disposable) {
                // Do nothing.
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
            }
        })
    }

    private fun getKey(deckId: String): String {
        return if (deckId.isEmpty()) "card-database" else deckId
    }

    private fun getCardFilterForFaction(faction: GwentFaction): CardFilter {
        return CardFilter(
            GwentCardRarity.values().map { it to true }.toMap(),
            mapOf(GwentCardColour.BRONZE to true, GwentCardColour.GOLD to true),
            mapOf(faction to true, GwentFaction.NEUTRAL to true),
            mapOf(
                GwentCardType.Unit to true,
                GwentCardType.Spell to true,
                GwentCardType.Artifact to true
            ),
            0,
            20,
            true,
            SortedBy.ALPHABETICALLY_ASC)
    }

    private fun getDefaultCardFilter(): CardFilter = CardFilter(
        GwentCardRarity.values().map { it to true }.toMap(),
        GwentCardColour.values().map { it to true }.toMap(),
        GwentFaction.values().map { it to true }.toMap(),
        mapOf(
            GwentCardType.Unit to true,
            GwentCardType.Spell to true,
            GwentCardType.Artifact to true,
            GwentCardType.Strategem to true,
            GwentCardType.Leader to true
        ), 0,
        20, false,
        SortedBy.ALPHABETICALLY_ASC)
}
package com.jamieadkins.decktracker.data

import android.annotation.SuppressLint
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.DeckConstants
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.DeckTrackerRepository
import com.jamieadkins.gwent.domain.tracker.predictions.CardPrediction
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictorRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DeckTrackerRepositoryImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val cardPredictorRepository: CardPredictorRepository,
    private val schedulerProvider: SchedulerProvider
) : DeckTrackerRepository {

    private val currentLeader = BehaviorSubject.create<GwentCard>()
    private val opponentCardsPlayed = BehaviorSubject.createDefault(emptyList<GwentCard>())

    override fun newGame(opponentFaction: GwentFaction, opponentLeaderId: String) {
        opponentCardsPlayed.onNext(emptyList())
        cardRepository.getCard(opponentLeaderId)
            .subscribeOn(schedulerProvider.io())
            .subscribe(currentLeader)
    }

    override fun trackOpponentCard(cardId: String): Completable {
        return Single.zip(
            opponentCardsPlayed.first(emptyList()),
            cardRepository.getCard(cardId).subscribeOn(schedulerProvider.io()).firstOrError(),
            BiFunction { cardsPlayed: List<GwentCard>, cardPlayed: GwentCard ->
                cardsPlayed + cardPlayed
            }
        )
            .flatMap { Single.fromCallable { opponentCardsPlayed.onNext(it) } }
            .ignoreElement()
    }

    override fun removeOpponentCard(cardId: String): Completable {
       return Single.zip(
            opponentCardsPlayed.first(emptyList()),
            cardRepository.getCard(cardId).subscribeOn(schedulerProvider.io()).firstOrError(),
            BiFunction { cardsPlayed: List<GwentCard>, cardPlayed: GwentCard ->
                cardsPlayed - cardPlayed
            }
        )
            .flatMap { Single.fromCallable { opponentCardsPlayed.onNext(it) } }
            .ignoreElement()
    }

    override fun observeGame(): Observable<DeckTrackerAnalysis> {
        return Observable.combineLatest(
            currentLeader.distinctUntilChanged(),
            opponentCardsPlayed.distinctUntilChanged(),
            BiFunction { leader: GwentCard, cardsPlayed: List<GwentCard> ->
                val startingProvisions = DeckConstants.BASE_PROVISION_ALLOWANCE + leader.extraProvisions
                val spentProvisions = cardsPlayed.map { it.provisions }.sum()
                val remainingProvisions = startingProvisions - spentProvisions
                val remainingCards = DeckConstants.CARDS_IN_DECK - cardsPlayed.size
                val averageProvisions = remainingProvisions.toFloat() / remainingCards

                DeckTrackerAnalysis(leader, cardsPlayed, remainingProvisions, averageProvisions)
            }
        )
    }

    override fun getCardsPlayed(): Observable<List<GwentCard>> = opponentCardsPlayed.distinctUntilChanged()

    override fun observePredictions(): Observable<CardPredictions> {
        return Observable.combineLatest(
            currentLeader.distinctUntilChanged(),
            opponentCardsPlayed
                .filter { it.isNotEmpty() }
                .distinctUntilChanged(),
            BiFunction { leader: GwentCard, cardsPlayed: List<GwentCard> ->
                leader.id to cardsPlayed.map { it.id }
            }
        )
            .switchMapMaybe {
                cardPredictorRepository.getPredictions(it.first, it.second)
                    .subscribeOn(schedulerProvider.io())
            }

    }
}
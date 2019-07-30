package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface DeckTrackerRepository {

    fun newGame(opponentFaction: GwentFaction, opponentLeaderId: String)

    fun observeGame(): Observable<DeckTrackerAnalysis>

    fun observePredictions(): Observable<CardPredictions>

    fun getCardsPlayed(): Observable<List<GwentCard>>

    fun trackOpponentCard(cardId: String): Completable

    fun removeOpponentCard(cardId: String): Completable
}
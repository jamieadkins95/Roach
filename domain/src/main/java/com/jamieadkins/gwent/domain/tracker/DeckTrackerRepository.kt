package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import io.reactivex.Observable
import io.reactivex.Single

interface DeckTrackerRepository {

    fun newGame(opponentFaction: GwentFaction, opponentLeaderId: String)

    fun observeGame(): Observable<DeckTrackerAnalysis>

    fun observePredictions(): Observable<CardPredictions>

    fun trackOpponentCard(cardId: String)

    fun removeOpponentCard(cardId: String)
}
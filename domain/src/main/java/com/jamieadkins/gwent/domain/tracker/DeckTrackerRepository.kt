package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import io.reactivex.Observable
import io.reactivex.Single

interface DeckTrackerRepository {

    fun newGame(opponentFaction: GwentFaction, opponentLeaderId: Long): Single<Long>

    fun observeGame(gameId: Long): Observable<DeckTrackerAnalysis>

    fun trackOpponentCard(gameId: Long, cardId: Long)
}
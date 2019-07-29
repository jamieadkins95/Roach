package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.base.MvpPresenter
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions

interface DeckTrackerContract {

    interface View {

        fun showFaction(faction: GwentFaction)

        fun showCardsPlayed(cards: List<GwentCard>)

        fun showDeckAnalysis(opponentProvisionsRemaining: Int, opponentAverageProvisionsRemaining: Float)

        fun showPredictions(cardPredictions: CardPredictions)
    }

    interface Presenter : MvpPresenter {

        fun init(faction: GwentFaction, leaderId: String)

        fun onOpponentCardPlayed(cardId: String)

        fun onOpponentCardDeleted(cardId: String)

        fun onSimilarDeckClicked(deckId: String)

        fun onCardClicked(cardId: String)
    }
}

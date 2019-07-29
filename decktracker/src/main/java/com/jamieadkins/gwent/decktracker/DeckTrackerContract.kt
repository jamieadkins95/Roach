package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.base.MvpPresenter
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions

interface DeckTrackerContract {

    interface View {

        fun showFaction(faction: GwentFaction)

        fun showDeckAnalysis(analysis: DeckTrackerAnalysis)

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

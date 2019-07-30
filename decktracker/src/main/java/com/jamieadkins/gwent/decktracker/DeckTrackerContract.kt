package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.base.MvpPresenter
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.predictions.CardPrediction
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import com.jamieadkins.gwent.domain.tracker.predictions.SimilarDeck

interface DeckTrackerContract {

    interface View {

        fun showFaction(faction: GwentFaction)

        fun showLeader(card: GwentCard)

        fun showCardsPlayed(cards: List<GwentCard>)

        fun showDeckAnalysis(opponentProvisionsRemaining: Int, opponentAverageProvisionsRemaining: Float)

        fun showPredictions(cardPredictions: List<CardPrediction>)

        fun showSimilarDecks(similar: List<SimilarDeck>)

        fun openCardDetails(cardId: String)

        fun openSimilarDeck(deckUrl: String)

        fun openFeedback()

        fun openGwentDeckLibrary()
    }

    interface Presenter : MvpPresenter {

        fun init(faction: GwentFaction, leaderId: String)

        fun onOpponentCardPlayed(cardId: String)

        fun onOpponentCardDeleted(cardId: String)

        fun onSimilarDeckClicked(deck: SimilarDeck)

        fun onCardClicked(cardId: String)

        fun onFeedbackClicked()
    }
}

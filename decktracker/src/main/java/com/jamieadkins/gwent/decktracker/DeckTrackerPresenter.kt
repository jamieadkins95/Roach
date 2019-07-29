package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.decktracker.bus.DeckTrackerEvent
import com.jamieadkins.gwent.decktracker.bus.DeckTrackerEvents
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardType
import com.jamieadkins.gwent.domain.tracker.AddCardToDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.GetCardPredictionsUseCase
import com.jamieadkins.gwent.domain.tracker.ObserveDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.RemoveCardFromDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.StartDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import javax.inject.Inject

class DeckTrackerPresenter @Inject constructor(
    private val view: DeckTrackerContract.View,
    private val startDeckTrackerUseCase: StartDeckTrackerUseCase,
    private val observeDeckTrackerUseCase: ObserveDeckTrackerUseCase,
    private val getCardPredictionsUseCase: GetCardPredictionsUseCase,
    private val addCardToDeckTrackerUseCase: AddCardToDeckTrackerUseCase,
    private val removeCardFromDeckTrackerUseCase: RemoveCardFromDeckTrackerUseCase
) : BasePresenter(), DeckTrackerContract.Presenter {

    override fun onAttach() {
        DeckTrackerEvents.register(DeckTrackerEvent.CardPlayed::class.java)
            .subscribe { addCardToDeckTrackerUseCase.addCard(it.cardId) }
            .addToComposite()

        observeDeckTrackerUseCase.observe()
            .subscribeWith(object : BaseDisposableObserver<DeckTrackerAnalysis>() {
                override fun onNext(analysis: DeckTrackerAnalysis) {
                    val cardsPlayed = analysis.opponentCardsPlayed.sortedByDescending { it.provisions }
                    view.showCardsPlayed(cardsPlayed)
                    view.showDeckAnalysis(analysis.opponentProvisionsRemaining, analysis.opponentAverageProvisionsRemaining)
                }
            })
            .addToComposite()

        getCardPredictionsUseCase.observe()
            .subscribeWith(object : BaseDisposableObserver<CardPredictions>() {
                override fun onNext(predictions: CardPredictions) {
                    val cards = predictions.cards
                        .filter { it.card.type != GwentCardType.Leader }
                        .sortedByDescending { it.percentage }
                        .take(20)
                    view.showPredictions(cards)
                    view.showSimilarDecks(predictions.similarDecksFound)
                }
            })
            .addToComposite()
    }

    override fun init(faction: GwentFaction, leaderId: String) {
        startDeckTrackerUseCase.newGame(faction, leaderId)
        view.showFaction(faction)
    }

    override fun onOpponentCardPlayed(cardId: String) = addCardToDeckTrackerUseCase.addCard(cardId)

    override fun onOpponentCardDeleted(cardId: String) = removeCardFromDeckTrackerUseCase.remove(cardId)

    override fun onSimilarDeckClicked(deckId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCardClicked(cardId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

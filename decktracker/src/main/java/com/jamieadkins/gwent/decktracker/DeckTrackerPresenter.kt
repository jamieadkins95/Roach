package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.tracker.DeckTrackerAnalysis
import com.jamieadkins.gwent.domain.tracker.GetCardPredictionsUseCase
import com.jamieadkins.gwent.domain.tracker.ObserveDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.StartDeckTrackerUseCase
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import javax.inject.Inject

class DeckTrackerPresenter @Inject constructor(
    private val view: DeckTrackerContract.View,
    private val startDeckTrackerUseCase: StartDeckTrackerUseCase,
    private val observeDeckTrackerUseCase: ObserveDeckTrackerUseCase,
    private val getCardPredictionsUseCase: GetCardPredictionsUseCase
) : BasePresenter(), DeckTrackerContract.Presenter {

    override fun onAttach() {
        observeDeckTrackerUseCase.observe()
            .subscribeWith(object : BaseDisposableObserver<DeckTrackerAnalysis>() {
                override fun onNext(analysis: DeckTrackerAnalysis) {
                    view.showDeckAnalysis(analysis)
                }
            })
            .addToComposite()

        getCardPredictionsUseCase.observe()
            .subscribeWith(object : BaseDisposableObserver<CardPredictions>() {
                override fun onNext(predictions: CardPredictions) {
                    view.showPredictions(predictions)
                }
            })
            .addToComposite()
    }

    override fun init(faction: GwentFaction, leaderId: String) {
        startDeckTrackerUseCase.newGame(faction, leaderId)
        view?.showFaction(faction)
    }

    override fun onOpponentCardPlayed(cardId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOpponentCardDeleted(cardId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSimilarDeckClicked(deckId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCardClicked(cardId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

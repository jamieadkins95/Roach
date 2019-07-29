package com.jamieadkins.gwent.decktracker.cardpicker

import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.decktracker.bus.DeckTrackerEvent
import com.jamieadkins.gwent.decktracker.bus.DeckTrackerEvents
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardType
import javax.inject.Inject

class CardPickerPresenter @Inject constructor(
    private val view: CardPickerContract.View,
    private val getCardsUseCase: GetCardsUseCase
) : BasePresenter(), CardPickerContract.Presenter {

    override fun onAttach() {
        // Do nothing.
    }

    override fun search(query: String, faction: GwentFaction) {
        if (query.isEmpty()) return
        getCardsUseCase.quickSearchCards(query, listOf(GwentFaction.NEUTRAL, faction))
            .first(emptyList())
            .map { it.filter { card -> card.collectible && card.type != GwentCardType.Leader } }
            .subscribeWith(object : BaseDisposableSingle<List<GwentCard>>() {
                override fun onSuccess(cards: List<GwentCard>) {
                    view.showCards(cards)
                }
            })
            .addToComposite()
    }

    override fun onCardPicked(cardId: String) {
        DeckTrackerEvents.post(DeckTrackerEvent.CardPlayed(cardId))
        view.close()
    }
}

package com.jamieadkins.gwent.decktracker.cardpicker

import com.jamieadkins.gwent.base.MvpPresenter
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard

interface CardPickerContract {

    interface View {

        fun showCards(cards: List<GwentCard>)

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun search(query: String, faction: GwentFaction)

        fun onCardPicked(cardId: String)
    }
}

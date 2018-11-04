package com.jamieadkins.gwent.deck.detail.leader

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.MvpPresenter

interface LeaderPickerContract {

    interface View {

        fun showLeaders(cards: List<GwentCard>)

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun setDeckId(deckId: String)
    }
}

package com.jamieadkins.gwent.deck.detail.leader

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.base.MvpPresenter

interface LeaderPickerContract {

    interface View {

        fun showLeaders(cards: List<GwentCard>)

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun setDeckId(deckId: String)
    }
}

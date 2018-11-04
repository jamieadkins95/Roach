package com.jamieadkins.gwent.deck.detail.rename

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.MvpPresenter

interface RenameDeckContract {

    interface View {

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun setDeckId(deckId: String)

        fun renameDeck(name: String)
    }
}

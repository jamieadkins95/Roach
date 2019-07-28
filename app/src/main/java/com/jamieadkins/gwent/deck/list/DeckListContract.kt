package com.jamieadkins.gwent.deck.list

import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.base.MvpPresenter

interface DeckListContract {

    interface View {

        fun showDecks(decks: List<GwentDeck>)

        fun showDeckDetails(deckId: String)

        fun showLoadingIndicator(loading: Boolean)
    }

    interface Presenter : MvpPresenter
}

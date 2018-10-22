package com.jamieadkins.gwent.deck.list

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.MvpPresenter

interface DeckListContract {

    interface View {

        fun showDecks(decks: List<GwentDeck>)

        fun showDeckDetails(deckId: String)

        fun showLoadingIndicator(loading: Boolean)
    }

    interface Presenter : MvpPresenter {

        fun createDeck(name: String, faction: GwentFaction)
    }
}

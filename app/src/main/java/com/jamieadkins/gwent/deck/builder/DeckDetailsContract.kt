package com.jamieadkins.gwent.deck.builder

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.base.MvpPresenter

interface DeckDetailsContract {

    interface View {

        fun showDeck(deck: GwentDeck)

        fun showCardDatabase(cards: List<GwentCard>, searchQuery: String)

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showLeaderPicker()

        fun showRenameDeckMenu()

        fun close()

        fun showCardDetails(cardId: String)

        fun showMaximumCardCountReached()
    }

    interface Presenter : MvpPresenter {

        fun setDeckId(deckId: String)

        fun onChangeLeaderClicked()

        fun onRenameClicked()

        fun onDeleteClicked()

        fun search(query: String)

        fun clearSearch()
    }
}

package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.deck.detail.DeckDetailsContract

/**
 * Specifies the contract between the view and the presenter.
 */

interface UserDeckDetailsContract {
    interface View : DeckDetailsContract.DeckDetailsView {
        fun showPotentialLeaders(potentialLeaders: List<CardDetails>)
    }

    interface Presenter : DeckDetailsContract.Presenter {

        fun publishDeck()

        fun changeLeader(leaderId: String)

        fun renameDeck(name: String)

        fun deleteDeck()
    }
}

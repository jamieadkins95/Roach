package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.gwent.data.CardDetails

/**
 * Specifies the contract between the view and the presenter.
 */

interface UserDeckDetailsContract {
    interface View : DeckBuilderContract.View {
        fun showPotentialLeaders(potentialLeaders: List<CardDetails>)
    }

    interface Presenter : DeckBuilderContract.Presenter {

        fun publishDeck()

        fun changeLeader(leaderId: String)

        fun renameDeck(name: String)

        fun deleteDeck()
    }
}

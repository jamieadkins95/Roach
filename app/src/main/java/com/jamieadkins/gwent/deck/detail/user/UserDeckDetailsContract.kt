package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.gwent.deck.detail.DeckDetailsContract
import com.jamieadkins.gwent.domain.card.model.GwentCard

/**
 * Specifies the contract between the view and the presenter.
 */

interface UserDeckDetailsContract {
    interface View : DeckDetailsContract.DeckDetailsView {
        fun showPotentialLeaders(potentialLeaders: List<GwentCard>)
    }

    interface Presenter : DeckDetailsContract.Presenter {

        fun changeLeader(leaderId: String)

        fun renameDeck(name: String)

        fun deleteDeck()
    }
}

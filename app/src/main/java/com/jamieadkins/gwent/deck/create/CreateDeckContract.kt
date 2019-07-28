package com.jamieadkins.gwent.deck.create

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.base.MvpPresenter

interface CreateDeckContract {

    interface View {

        fun showDeckDetails(deckId: String)

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun createDeck(name: String, faction: GwentFaction)
    }
}

package com.jamieadkins.gwent.deck.detail.rename

import com.jamieadkins.gwent.base.MvpPresenter

interface RenameDeckContract {

    interface View {

        fun close()
    }

    interface Presenter : MvpPresenter {

        fun setDeckId(deckId: String)

        fun renameDeck(name: String)
    }
}

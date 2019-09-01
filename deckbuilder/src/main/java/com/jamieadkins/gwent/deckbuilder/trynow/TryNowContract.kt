package com.jamieadkins.gwent.deckbuilder.trynow

import com.jamieadkins.gwent.base.MvpPresenter

interface TryNowContract {

    interface View {

        fun goToDeck(deckId: String)
    }

    interface Presenter : MvpPresenter
}

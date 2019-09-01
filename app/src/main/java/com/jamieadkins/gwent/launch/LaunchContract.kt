package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.base.MvpPresenter

interface LaunchContract {

    interface View {

        fun onSetupComplete()

        fun goToDeck(deckId: String)
    }

    interface Presenter : MvpPresenter {

        fun onAttach(tryNow: Boolean)
    }
}

package com.jamieadkins.gwent.card.detail

import com.jamieadkins.gwent.base.MvpPresenter

interface DetailContract {
    interface View {

        fun showScreen(cardDetailsScreenData: CardDetailsScreenData)

        fun showLoadingIndicator()

        fun hideLoadingIndicator()
    }

    interface Presenter : MvpPresenter {
        fun setCardId(cardId: String)
    }
}

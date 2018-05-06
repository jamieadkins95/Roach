package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BaseView

interface DetailContract {
    interface View : BaseView {

        fun showScreen(cardDetailsScreenData: CardDetailsScreenData)
    }

    interface Presenter
}

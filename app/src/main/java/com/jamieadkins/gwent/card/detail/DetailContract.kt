package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BaseView
import com.jamieadkins.gwent.core.GwentCard

interface DetailContract {
    interface View : BaseView {

        fun showScreen(cardDetailsScreenData: CardDetailsScreenData)
    }

    interface Presenter
}

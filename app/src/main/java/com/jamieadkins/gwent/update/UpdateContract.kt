package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.base.MvpPresenter

interface UpdateContract {

    interface View {

        fun finish()

        fun showError()
    }

    interface Presenter : MvpPresenter
}

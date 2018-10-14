package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.main.MvpPresenter

interface LaunchContract {

    interface View {

        fun onSetupComplete()
    }

    interface Presenter : MvpPresenter
}

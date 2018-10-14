package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.main.BasePresenter
import javax.inject.Inject

class LaunchPresenter @Inject constructor(
    private val view: LaunchContract.View
) : BasePresenter(), LaunchContract.Presenter {

    override fun onAttach() {
        view.onSetupComplete()
    }
}

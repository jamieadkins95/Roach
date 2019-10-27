package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.domain.launch.PerformFirstTimeSetupUseCase
import com.jamieadkins.gwent.base.BasePresenter
import io.reactivex.observers.DisposableCompletableObserver
import javax.inject.Inject

class LaunchPresenter @Inject constructor(
    private val view: LaunchContract.View,
    private val performFirstTimeSetupUseCase: PerformFirstTimeSetupUseCase
) : BasePresenter(), LaunchContract.Presenter {

    override fun onAttach() {
        performFirstTimeSetupUseCase.performFirstTimeSetup()
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() = view.onSetupComplete()

                override fun onError(e: Throwable) = view.onSetupComplete()
            })
            .addToComposite()
    }
}

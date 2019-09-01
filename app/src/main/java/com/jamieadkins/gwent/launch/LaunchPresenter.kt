package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.domain.launch.PerformFirstTimeSetupUseCase
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.domain.deck.GetTryNowDeckUseCase
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
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

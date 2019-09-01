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
    private val performFirstTimeSetupUseCase: PerformFirstTimeSetupUseCase,
    private val getTryNowDeckUseCase: GetTryNowDeckUseCase
) : BasePresenter(), LaunchContract.Presenter {

    override fun onAttach() = onAttach(false)

    override fun onAttach(tryNow: Boolean) {
        if (tryNow) {
            performFirstTimeSetupUseCase.performFirstTimeSetup()
                .andThen(getTryNowDeckUseCase.get())
                .subscribeWith(object : DisposableSingleObserver<String>() {
                    override fun onSuccess(deckId: String) = view.goToDeck(deckId)

                    override fun onError(e: Throwable) { Timber.e(e) }
                })
                .addToComposite()
        } else {
            performFirstTimeSetupUseCase.performFirstTimeSetup()
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() = view.onSetupComplete()

                    override fun onError(e: Throwable) = view.onSetupComplete()
                })
                .addToComposite()
        }
    }
}

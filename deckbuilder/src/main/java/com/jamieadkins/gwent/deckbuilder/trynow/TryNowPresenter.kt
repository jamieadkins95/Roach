package com.jamieadkins.gwent.deckbuilder.trynow

import com.jamieadkins.gwent.domain.launch.PerformFirstTimeSetupUseCase
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.domain.deck.GetTryNowDeckUseCase
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
import javax.inject.Inject

class TryNowPresenter @Inject constructor(
    private val view: TryNowContract.View,
    private val performFirstTimeSetupUseCase: PerformFirstTimeSetupUseCase,
    private val getTryNowDeckUseCase: GetTryNowDeckUseCase
) : BasePresenter(), TryNowContract.Presenter {

    override fun onAttach() {
        performFirstTimeSetupUseCase.performFirstTimeSetup()
            .andThen(getTryNowDeckUseCase.get())
            .subscribeWith(object : DisposableSingleObserver<String>() {
                override fun onSuccess(deckId: String) = view.goToDeck(deckId)

                override fun onError(e: Throwable) { Timber.e(e) }
            })
            .addToComposite()
    }
}

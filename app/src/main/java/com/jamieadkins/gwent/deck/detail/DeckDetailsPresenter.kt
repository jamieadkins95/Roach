package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.domain.deck.CreateDeckUseCase
import com.jamieadkins.gwent.domain.deck.DeleteDeckUseCase
import com.jamieadkins.gwent.domain.deck.GetDeckUseCase
import com.jamieadkins.gwent.domain.deck.RenameDeckUseCase
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DeckDetailsPresenter @Inject constructor(
    private val view: DeckDetailsContract.View,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val getDeckUseCase: GetDeckUseCase
) : BasePresenter(), DeckDetailsContract.Presenter {

    private val latestDeckId = BehaviorSubject.create<String>()

    override fun onAttach() {
        latestDeckId
            .switchMap(getDeckUseCase::get)
            .subscribeWith(object : BaseDisposableObserver<GwentDeck>() {
                override fun onNext(deck: GwentDeck) {
                    view.showDeck(deck)
                }
            })
            .addToComposite()
    }

    override fun setDeckId(deckId: String) {
        latestDeckId.onNext(deckId)
    }

    override fun onDeleteClicked() {
        latestDeckId
            .firstOrError()
            .flatMapCompletable(deleteDeckUseCase::delete)
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    view.close()
                }

                override fun onError(e: Throwable) {
                    // Do nothing.
                }
            })
            .addToComposite()
    }

    override fun onRenameClicked() {
        view.showRenameDeckMenu()
    }

    override fun onChangeLeaderClicked() {
        view.showLeaderPicker()
    }
}

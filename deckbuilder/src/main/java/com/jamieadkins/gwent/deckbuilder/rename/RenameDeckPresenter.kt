package com.jamieadkins.gwent.deckbuilder.rename

import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.domain.deck.RenameDeckUseCase
import com.jamieadkins.gwent.base.BasePresenter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RenameDeckPresenter @Inject constructor(
    private val view: RenameDeckContract.View,
    private val renameDeckUseCase: RenameDeckUseCase
) : BasePresenter(), RenameDeckContract.Presenter {

    private val latestDeckId = BehaviorSubject.create<String>()

    override fun onAttach() {
        // Do nothing
    }

    override fun setDeckId(deckId: String) {
        latestDeckId.onNext(deckId)
    }

    override fun renameDeck(name: String) {
        latestDeckId
            .flatMapCompletable { deckId ->
                renameDeckUseCase.rename(deckId, name)
                    .doOnComplete { view.close() }
            }
            .subscribeWith(object : BaseCompletableObserver() {
                override fun onComplete() {
                    // This will never complete
                }
            })
            .addToComposite()
    }
}

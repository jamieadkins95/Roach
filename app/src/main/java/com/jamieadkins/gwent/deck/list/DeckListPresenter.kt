package com.jamieadkins.gwent.deck.list

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.domain.deck.GetDecksUseCase
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.BasePresenter
import javax.inject.Inject

class DeckListPresenter @Inject constructor(
    private val view: DeckListContract.View,
    private val getDecksUseCase: GetDecksUseCase
) : BasePresenter(), DeckListContract.Presenter {

    override fun onAttach() {
        getDecksUseCase.get()
            .doOnSubscribe { view.showLoadingIndicator(true) }
            .subscribeWith(object : BaseDisposableObserver<List<GwentDeck>>() {
                override fun onNext(decks: List<GwentDeck>) {
                    view.showDecks(decks)
                    view.showLoadingIndicator(false)
                }
            })
            .addToComposite()
    }
}

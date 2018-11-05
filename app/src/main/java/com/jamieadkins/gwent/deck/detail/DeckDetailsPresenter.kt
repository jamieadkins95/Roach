package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.AddCardToDeckResult
import com.jamieadkins.gwent.domain.deck.AddCardToDeckUseCase
import com.jamieadkins.gwent.domain.deck.DeleteDeckUseCase
import com.jamieadkins.gwent.domain.deck.GetDeckUseCase
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class DeckDetailsPresenter @Inject constructor(
    private val view: DeckDetailsContract.View,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val getDeckUseCase: GetDeckUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val addCardToDeckUseCase: AddCardToDeckUseCase
) : BasePresenter(), DeckDetailsContract.Presenter {

    private val latestDeckId = BehaviorSubject.create<String>()
    private val searches = BehaviorSubject.createDefault("")

    override fun onAttach() {
        latestDeckId
            .switchMap(getDeckUseCase::get)
            .subscribeWith(object : BaseDisposableObserver<GwentDeck>() {
                override fun onNext(deck: GwentDeck) {
                    view.showDeck(deck)
                }
            })
            .addToComposite()

        searches
            .switchMap { query ->
                val cards = if (query.isEmpty()) {
                    getCardsUseCase.getCards()
                } else {
                    getCardsUseCase.searchCards(query)
                }

                cards.map { Pair(it, query) }
                    .doOnSubscribe { view.showLoadingIndicator() }
            }
            .subscribeWith(object : BaseDisposableObserver<Pair<List<GwentCard>, String>>() {
                override fun onNext(data: Pair<List<GwentCard>, String>) {
                    view.showCardDatabase(data.first, data.second)
                    view.hideLoadingIndicator()
                }
            })
            .addToComposite()


        Observable.combineLatest(
            latestDeckId,
            RxBus.register(GwentCardClickEvent::class.java),
            BiFunction { deckId: String, cardEvent: GwentCardClickEvent -> Pair(deckId, cardEvent.data) })
            .flatMapSingle {
                addCardToDeckUseCase.addCard(it.first, it.second)
            }
            .subscribeWith(object : BaseDisposableObserver<AddCardToDeckResult>() {
                override fun onNext(t: AddCardToDeckResult) {
                    // Do nothing.
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

package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.deck.DeckBuilderEvent
import com.jamieadkins.gwent.deck.DeckBuilderEvents
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.AddCardToDeckResult
import com.jamieadkins.gwent.domain.deck.AddCardToDeckUseCase
import com.jamieadkins.gwent.domain.deck.DeleteDeckUseCase
import com.jamieadkins.gwent.domain.deck.GetDeckUseCase
import com.jamieadkins.gwent.domain.deck.RemoveCardFromDeckUseCase
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.filter.FilterCardsUseCase
import com.jamieadkins.gwent.domain.filter.GetFilterUseCase
import com.jamieadkins.gwent.domain.filter.model.CardFilter
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
    private val filterCardsUseCase: FilterCardsUseCase,
    private val getFilterUseCase: GetFilterUseCase,
    private val addCardToDeckUseCase: AddCardToDeckUseCase,
    private val removeCardFromDeckUseCase: RemoveCardFromDeckUseCase
) : BasePresenter(), DeckDetailsContract.Presenter {

    private val latestDeckId = BehaviorSubject.create<String>()
    private val searches = BehaviorSubject.createDefault("")

    override fun onAttach() {

        DeckBuilderEvents.register()
            .subscribeWith(object : BaseDisposableObserver<DeckBuilderEvent>() {
                override fun onNext(event: DeckBuilderEvent) {
                    when (event) {
                        is DeckBuilderEvent.CardDatabaseLongClick,
                        is DeckBuilderEvent.DeckLongClick,
                        is DeckBuilderEvent.LeaderLongClick -> {
                            view.showCardDetails(event.cardId)
                        }
                        is DeckBuilderEvent.CardDatabaseClick -> {
                            addCardToDeck(event.cardId)
                        }
                        is DeckBuilderEvent.LeaderClick -> {
                            view.showLeaderPicker()
                        }
                        is DeckBuilderEvent.DeckClick -> {
                            removeCardFromDeck(event.cardId)
                        }
                    }
                }
            })
            .addToComposite()

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

                Observable.combineLatest(
                    cards,
                    latestDeckId.switchMap { getFilterUseCase.getFilter(it) },
                    BiFunction { cardList: List<GwentCard>, filter: CardFilter ->
                        Pair(cardList, filter)
                    }
                )
                    .flatMapSingle { filterCardsUseCase.filter(it.first, it.second) }
                    .map { Pair(it, query) }
                    .doOnSubscribe { view.showLoadingIndicator() }
            }
            .subscribeWith(object : BaseDisposableObserver<Pair<List<GwentCard>, String>>() {
                override fun onNext(data: Pair<List<GwentCard>, String>) {
                    view.showCardDatabase(data.first, data.second)
                    view.hideLoadingIndicator()
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

    override fun search(query: String) {
        searches.onNext(query)
    }

    override fun clearSearch() {
        searches.onNext("")
    }

    private fun addCardToDeck(cardId: String) {
        latestDeckId
            .firstOrError()
            .flatMap { deckId ->
                addCardToDeckUseCase.addCard(deckId, cardId)
            }
            .subscribeWith(object : BaseDisposableSingle<AddCardToDeckResult>() {
                override fun onSuccess(result: AddCardToDeckResult) {
                    // Do nothing.
                }
            })
            .addToComposite()
    }

    private fun removeCardFromDeck(cardId: String) {
        latestDeckId
            .firstOrError()
            .flatMapCompletable { deckId ->
                removeCardFromDeckUseCase.removeCard(deckId, cardId)
            }
            .subscribeWith(object : BaseCompletableObserver() {
                override fun onComplete() {
                    // Do nothing.
                }
            })
            .addToComposite()
    }
}

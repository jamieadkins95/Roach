package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.bus.ScrollToTopEvent
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.domain.filter.FilterCardsUseCase
import com.jamieadkins.gwent.domain.filter.GetFilterUseCase
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.update.model.Notice
import com.jamieadkins.gwent.domain.update.repository.GetCardDatabaseUpdateUseCase
import com.jamieadkins.gwent.domain.update.repository.GetNoticesUseCase
import com.jamieadkins.gwent.domain.update.repository.StartCardDatabaseUpdateUseCase
import com.jamieadkins.gwent.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CardDatabasePresenter @Inject constructor(
    private val view: CardDatabaseContract.View,
    private val getCardsUseCase: GetCardsUseCase,
    private val filterCardsUseCase: FilterCardsUseCase,
    private val getFilterUseCase: GetFilterUseCase,
    private val getCardDatabaseUpdateUseCase: GetCardDatabaseUpdateUseCase,
    private val startCardDatabaseUpdateUseCase: StartCardDatabaseUpdateUseCase,
    private val getNoticesUseCase: GetNoticesUseCase
) : BasePresenter(), CardDatabaseContract.Presenter {

    private val searches = BehaviorSubject.createDefault("")

    override fun onAttach() {
        RxBus.register(ScrollToTopEvent::class.java)
            .subscribeWith(object : BaseDisposableObserver<ScrollToTopEvent>() {
                override fun onNext(t: ScrollToTopEvent) {
                    view.scrollToTop()
                }
            })
            .addToComposite()

        RxBus.register(GwentCardClickEvent::class.java)
            .subscribeWith(object : BaseDisposableObserver<GwentCardClickEvent>() {
                override fun onNext(event: GwentCardClickEvent) {
                    view.showCardDetails(event.data)
                }
            })
            .addToComposite()

        val getCards = searches
            .switchMap { query ->
                val isSearch = query.isNotEmpty()
                val cards = if (query.isEmpty()) {
                    getCardsUseCase.getCards()
                } else {
                    getCardsUseCase.searchCards(query)
                }

                Observable.combineLatest(
                    cards,
                    getFilterUseCase.getFilter(),
                    BiFunction { cardList: List<GwentCard>, filter: CardFilter ->
                        Pair(cardList, filter)
                    }
                )
                    .flatMapSingle { filterCardsUseCase.filter(it.first, it.second, isSearch) }
                    .map { Pair(it, query) }
                    .doOnSubscribe { view.showLoadingIndicator(true) }
            }

        Observable.combineLatest(getCards,
                                 getNotices(),
                                 BiFunction { pair: Pair<List<GwentCard>, String>, notices: List<Notice> ->
                                     CardDatabaseScreenModel(pair.first, pair.second, notices)
                                 })
            .subscribeWith(object : BaseDisposableObserver<CardDatabaseScreenModel>() {
                override fun onNext(data: CardDatabaseScreenModel) {
                    view.showData(data)
                    view.showLoadingIndicator(false)
                }
            })
            .addToComposite()

        getUpdates()
            .subscribeWith(object : BaseDisposableObserver<Boolean>() {
                override fun onNext(update: Boolean) {
                    if (update) {
                        view.openUpdateScreen()
                    }
                }
            })
            .addToComposite()
    }

    private fun getUpdates(): Observable<Boolean> {
        return refreshRequests
            .switchMap {
                getCardDatabaseUpdateUseCase.isUpdateAvailable()
            }
            .startWith(false)
    }

    private fun getNotices(): Observable<List<Notice>> {
        return refreshRequests
            .switchMap { getNoticesUseCase.getNotices() }
            .startWith(emptyList<Notice>())
    }

    override fun search(query: String) {
        searches.onNext(query)
    }

    override fun clearSearch() {
        searches.onNext("")
    }
}

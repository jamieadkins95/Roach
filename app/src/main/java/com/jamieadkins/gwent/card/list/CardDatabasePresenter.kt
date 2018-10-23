package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.DownloadUpdateClickEvent
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.bus.ResetFiltersEvent
import com.jamieadkins.gwent.bus.ScrollToTopEvent
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.domain.update.repository.GetCardDatabaseUpdateUseCase
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CardDatabasePresenter @Inject constructor(
    private val view: CardDatabaseContract.View,
    private val getCardsUseCase: GetCardsUseCase,
    private val getCardDatabaseUpdateUseCase: GetCardDatabaseUpdateUseCase
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

        RxBus.register(DownloadUpdateClickEvent::class.java)
            .subscribeWith(object : BaseDisposableObserver<DownloadUpdateClickEvent>() {
                override fun onNext(event: DownloadUpdateClickEvent) {
                    view.openUpdateScreen()
                }
            })
            .addToComposite()

        val getCards = searches
            .switchMap { query ->
                val cards = if (query.isEmpty()) {
                    getCardsUseCase.getCards()
                } else {
                    getCardsUseCase.searchCards(query)
                }

                cards.map { Pair(it, query) }
                    .doOnSubscribe { view.showLoadingIndicator(true) }
            }

        Observable.combineLatest(getCards,
                                 getUpdates(),
                                 BiFunction { pair: Pair<List<GwentCard>, String>, updateAvaliable: Boolean ->
                                     CardDatabaseScreenModel(pair.first, pair.second, updateAvaliable)
                                 })
            .subscribeWith(object : BaseDisposableObserver<CardDatabaseScreenModel>() {
                override fun onNext(data: CardDatabaseScreenModel) {
                    view.showData(data)
                    view.showLoadingIndicator(false)
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

    override fun search(query: String) {
        searches.onNext(query)
    }

    override fun clearSearch() {
        searches.onNext("")
    }
}

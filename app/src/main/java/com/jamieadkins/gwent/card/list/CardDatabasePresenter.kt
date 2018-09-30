package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.bus.RefreshEvent
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.DownloadUpdateClickEvent
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.bus.ResetFiltersEvent
import com.jamieadkins.gwent.bus.ScrollToTopEvent
import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class CardDatabasePresenter @Inject constructor(
    private val view: CardDatabaseContract.View,
    private val schedulerProvider: BaseSchedulerProvider,
    private val cardRepository: CardRepository,
    private val updateRepository: UpdateRepository,
    val filterRepository: FilterRepository
) : BasePresenter(), CardDatabaseContract.Presenter {

    init {
        filterRepository.setDefaultFilters()
    }

    override fun onAttach() {
        RxBus.register(ResetFiltersEvent::class.java)
            .subscribeWith(object : BaseDisposableObserver<ResetFiltersEvent>() {
                override fun onNext(event: ResetFiltersEvent) {
                    filterRepository.resetFilters()
                }
            })
            .addToComposite()

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

        Observable.combineLatest(getCards(),
                                 getUpdates(),
                                 BiFunction { cards: CardDatabaseResult, updateAvaliable: Boolean ->
                                     CardDatabaseScreenModel(cards, updateAvaliable)
                                 })
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { view.setLoadingIndicator(true) }
            .subscribeWith(object : BaseDisposableObserver<CardDatabaseScreenModel>() {
                override fun onNext(data: CardDatabaseScreenModel) {
                    view.showData(data)
                    view.setLoadingIndicator(false)
                }
            })
            .addToComposite()
    }

    private fun getCards(): Observable<CardDatabaseResult> {
        val filter = filterRepository.getFilter()
            .doOnNext {
                view.setLoadingIndicator(true)
            }
        return Observable.combineLatest(refreshRequests,
                                        filter,
                                        BiFunction { _: Any, filter: CardFilter -> filter })
            .observeOn(schedulerProvider.io())
            .switchMapSingle { cardRepository.getCards(it) }
    }

    private fun getUpdates(): Observable<Boolean> {
        return refreshRequests
            .flatMapSingle { updateRepository.isUpdateAvailable() }
            .startWith(false)
    }

    override fun search(query: String) {
        filterRepository.updateSearchQuery(query)
    }

    override fun clearSearch() {
        filterRepository.clearSearchQuery()
    }
}

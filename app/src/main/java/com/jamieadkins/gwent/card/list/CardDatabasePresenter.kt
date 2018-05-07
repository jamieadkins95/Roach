package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.bus.RefreshEvent
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.bus.DownloadUpdateClickEvent
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CardDatabasePresenter(schedulerProvider: BaseSchedulerProvider,
                            val cardRepository: CardRepository,
                            val updateRepository: UpdateRepository,
                            val filterRepository: FilterRepository) :
        BasePresenter<CardDatabaseContract.View>(schedulerProvider), CardDatabaseContract.Presenter {

    init {
        filterRepository.setDefaultFilters()
    }

    override fun onAttach(newView: CardDatabaseContract.View) {
        super.onAttach(newView)

        RxBus.register(ResetFiltersEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<ResetFiltersEvent>() {
                    override fun onNext(event: ResetFiltersEvent) {
                        filterRepository.resetFilters()
                    }
                })
                .addToComposite(disposable)

        RxBus.register(ScrollToTopEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<ScrollToTopEvent>() {
                    override fun onNext(t: ScrollToTopEvent) {
                        view?.scrollToTop()
                    }
                })
                .addToComposite(disposable)

        RxBus.register(GwentCardClickEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<GwentCardClickEvent>() {
                    override fun onNext(event: GwentCardClickEvent) {
                        view?.showCardDetails(event.data)
                    }
                })
                .addToComposite(disposable)

        RxBus.register(DownloadUpdateClickEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<DownloadUpdateClickEvent>() {
                    override fun onNext(event: DownloadUpdateClickEvent) {
                        view?.openUpdateScreen()
                    }
                })
                .addToComposite(disposable)

        Observable.combineLatest(getCards(), getUpdates().startWith(false),
                BiFunction { cards: CardDatabaseResult, updateAvaliable: Boolean ->
                    CardDatabaseScreenModel(cards, updateAvaliable)
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view?.setLoadingIndicator(true) }
                .subscribeWith(object : BaseDisposableObserver<CardDatabaseScreenModel>() {
                    override fun onNext(data: CardDatabaseScreenModel) {
                        view?.showData(data)
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    private fun getCards(): Observable<CardDatabaseResult> {
        return Observable.combineLatest(RxBus.register(RefreshEvent::class.java).startWith(RefreshEvent()),
                filterRepository.getFilter(), BiFunction { event: RefreshEvent, filter: CardFilter -> filter })
                .observeOn(schedulerProvider.io())
                .switchMapSingle { cardRepository.getCards(it) }
    }

    private fun getUpdates(): Observable<Boolean> {
        return RxBus.register(RefreshEvent::class.java).startWith(RefreshEvent())
                .flatMapSingle { updateRepository.isUpdateAvailable() }
    }

    override fun search(query: String) {
        filterRepository.updateSearchQuery(query)
    }

    override fun clearSearch() {
        filterRepository.clearSearchQuery()
    }
}

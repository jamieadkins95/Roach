package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.filter.FilterRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

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

        RxBus.register(RefreshEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<RefreshEvent>() {
                    override fun onNext(t: RefreshEvent) {
                        onRefresh()
                    }
                })
                .addToComposite(disposable)

        RxBus.register(ResetFiltersEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<ResetFiltersEvent>() {
                    override fun onNext(event: ResetFiltersEvent) {
                        filterRepository.resetFilters()
                    }
                })
                .addToComposite(disposable)

        filterRepository.getFilter()
                .observeOn(schedulerProvider.ui())
                .doOnNext { view?.setLoadingIndicator(true) }
                .observeOn(schedulerProvider.io())
                .switchMapSingle { cardRepository.getCards(it) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableObserver<Collection<GwentCard>>() {
                    override fun onNext(list: Collection<GwentCard>) {
                        view?.showCards(list.toMutableList())
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        updateRepository.isUpdateAvailable()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Boolean>() {
                    override fun onSuccess(update: Boolean) {
                        if (update) {
                            view?.showUpdateAvailable()
                        }
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun search(query: String) {
        view?.setLoadingIndicator(true)
        cardRepository.searchCards(query)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(list: Collection<GwentCard>) {
                        view?.showSearchResults(query, list.toMutableList())
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun clearSearch() {

    }
}

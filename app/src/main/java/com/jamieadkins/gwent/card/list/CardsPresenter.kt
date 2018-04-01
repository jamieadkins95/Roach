package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.bus.RefreshEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(schedulerProvider: BaseSchedulerProvider,
                     val cardRepository: CardRepository,
                     val updateRepository: UpdateRepository) :
        BasePresenter<CardDatabaseContract.View>(schedulerProvider), CardDatabaseContract.Presenter {

    private val cardFilter = CardFilter()

    override fun onAttach(newView: CardDatabaseContract.View) {
        super.onAttach(newView)

        RxBus.register(RefreshEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<RefreshEvent>() {
                    override fun onNext(t: RefreshEvent) {
                        onRefresh()
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
                    }
                })
                .addToComposite(disposable)

        loadCardData()
    }

    private fun loadCardData() {
        view?.setLoadingIndicator(true)
        val source = if (cardFilter.searchQuery != null) {
            cardRepository.searchCards(cardFilter.searchQuery ?: "")
        } else {
            cardRepository.getCards(cardFilter)
        }
        source
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(list: Collection<GwentCard>) {
                        view?.showCards(list.toMutableList())
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun search(query: String?) {
        cardFilter.searchQuery = query
        loadCardData()
    }

    override fun clearSearch() {
        cardFilter.searchQuery = null
        loadCardData()
    }
}

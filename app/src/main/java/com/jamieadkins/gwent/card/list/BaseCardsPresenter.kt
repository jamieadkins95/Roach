package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.base.BaseDisposableCompletableObserver
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.filter.FilterProvider
import com.jamieadkins.gwent.model.GwentCard
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

abstract class BaseCardsPresenter<T : CardsContract.View>(private val mCardsInteractor: CardsInteractor) :
        BaseFilterPresenter<T>(), CardsContract.Presenter {

    val cardRepository = Injection.provideCardRepository()
    val updateRepository = Injection.provideUpdateRepository()

    override fun onRefresh() {
        onLoadData()
    }

    override fun onAttach(newView: T) {
        super.onAttach(newView)
        FilterProvider.getCardFilter()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { view?.setLoadingIndicator(true) }
                .observeOn(Schedulers.io())
                .flatMapSingle { cardRepository.getCards(it) }
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<Collection<GwentCard>>() {
                    override fun onNext(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }
                })
                .addToComposite(disposable)
    }

    open fun onLoadData() {
        view?.setLoadingIndicator(true)
        updateRepository.isUpdateAvailable()
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Boolean>() {
                    override fun onSuccess(update: Boolean) {
                        if (update) {
                            view?.showUpdateAvailable()
                        }
                    }
                })
                .addToComposite(disposable)

        cardRepository.getCards(cardFilter)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }
                })
                .addToComposite(disposable)
    }

    private fun onNewCardList(cardList: Collection<GwentCard>) {
        view?.showCards(cardList.toList())
        view?.setLoadingIndicator(false)
    }

    override fun onCardFilterUpdated() {
        FilterProvider.updateFilter(cardFilter)
    }

    override fun updateSearchQuery(query: String?) {
        super.updateSearchQuery(query)
        view?.setLoadingIndicator(true)
        val source = if (!searchQuery.isNullOrEmpty()) {
            cardRepository.searchCards(searchQuery!!)
        } else {
            cardRepository.getCards(cardFilter)
        }
        source.applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }
                })
                .addToComposite(disposable)
    }
}

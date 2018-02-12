package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.filter.FilterProvider
import com.jamieadkins.gwent.model.GwentCard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseCardsPresenter<T : CardsContract.View>(schedulerProvider: BaseSchedulerProvider,
                                                          private val cardRepository: CardRepository,
                                                          private val updateRepository: UpdateRepository) :
        BaseFilterPresenter<T>(schedulerProvider), CardsContract.Presenter {

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

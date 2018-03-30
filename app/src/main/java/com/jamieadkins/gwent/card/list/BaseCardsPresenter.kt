package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.model.GwentCard

abstract class BaseCardsPresenter<T : CardsContract.View>(schedulerProvider: BaseSchedulerProvider,
                                                          val cardRepository: CardRepository,
                                                          val updateRepository: UpdateRepository) :
        BaseFilterPresenter<T>(schedulerProvider), CardsContract.Presenter {

    override fun onRefresh() {
        onLoadData()
    }

    open fun onLoadData() {
        view?.setLoadingIndicator(true)

        val source = if (!searchQuery.isNullOrEmpty()) {
            cardRepository.searchCards(searchQuery!!)
        } else {
            cardRepository.getCards(cardFilter)
        }
        /*source.applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }
                })
                .addToComposite(disposable)*/
    }

    private fun onNewCardList(cardList: Collection<GwentCard>) {
        view?.showCards(cardList.toList())
        view?.setLoadingIndicator(false)
    }

    override fun onCardFilterUpdated() {
        onLoadData()
    }

    override fun updateSearchQuery(query: String?) {
        super.updateSearchQuery(query)
        view?.setLoadingIndicator(true)
        val source = if (!searchQuery.isNullOrEmpty()) {
            cardRepository.searchCards(searchQuery!!)
        } else {
            cardRepository.getCards(cardFilter)
        }
        /*source.applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }
                })
                .addToComposite(disposable)*/
    }
}

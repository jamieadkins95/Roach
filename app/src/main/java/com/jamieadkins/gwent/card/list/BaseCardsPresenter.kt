package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.Injection
import com.jamieadkins.gwent.base.*
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.filter.FilterProvider
import com.jamieadkins.gwent.model.GwentCard
import com.jamieadkins.gwent.model.patch.PatchState
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import java.util.concurrent.TimeoutException

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

abstract class BaseCardsPresenter<T : CardsContract.View>(private val mCardsInteractor: CardsInteractor) :
        BaseFilterPresenter<T>(), CardsContract.Presenter {

    val cardRepository = Injection.provideCardRepository()

    override fun onRefresh() {
        onLoadData()
    }

    override fun onAttach(newView: T) {
        super.onAttach(newView)
        FilterProvider.getCardFilter()
                .flatMapSingle { cardRepository.getCards(it) }
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<Collection<GwentCard>>() {
                    override fun onNext(t: Collection<GwentCard>) {
                        onNewCardList(t)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    open fun onLoadData() {
        view?.setLoadingIndicator(true)
    }

    private fun onNewCardList(cardList: Collection<GwentCard>) {
        view?.showCards(cardList.toList())
        if (cardList.isEmpty()) {
            view?.showEmptyView()
        }
        view?.setLoadingIndicator(false)
    }

    override fun onCardFilterUpdated() {
        FilterProvider.updateFilter(cardFilter)
    }
}

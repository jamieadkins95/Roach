package com.jamieadkins.gwent.collection

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.CollectionEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

class CollectionPresenter(schedulerProvider: BaseSchedulerProvider,
                          val cardRepository: CardRepository,
                          val updateRepository: UpdateRepository) :
        BasePresenter<CollectionContract.View>(schedulerProvider), CollectionContract.Presenter {

    override fun onRefresh() {
    }

    override fun onAttach(newView: CollectionContract.View) {
        super.onAttach(newView)
    }
}

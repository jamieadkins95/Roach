package com.jamieadkins.gwent.collection

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository

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

package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.deck.detail.DeckBuilderContract

class CardDatabasePresenter(private val deckId: String,
                            private val deckRepository: DeckRepository,
                            schedulerProvider: BaseSchedulerProvider,
                            private val cardRepository: CardRepository,
                            private val updateRepository: UpdateRepository) :
        BasePresenter<DeckBuilderContract.CardDatabaseView>(schedulerProvider), DeckBuilderContract.Presenter {

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
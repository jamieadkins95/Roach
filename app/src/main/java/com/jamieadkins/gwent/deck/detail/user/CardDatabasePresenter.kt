package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.card.list.BaseCardsPresenter
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.deck.detail.DeckBuilderContract

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class CardDatabasePresenter(private val deckId: String,
                            private val deckRepository: DeckRepository,
                            schedulerProvider: BaseSchedulerProvider,
                            cardRepository: CardRepository,
                            updateRepository: UpdateRepository) :
        BaseCardsPresenter<DeckBuilderContract.CardDatabaseView>(schedulerProvider, cardRepository, updateRepository), DeckBuilderContract.Presenter {

    override fun onAttach(newView: DeckBuilderContract.CardDatabaseView) {
        super.onAttach(newView)
    }
}
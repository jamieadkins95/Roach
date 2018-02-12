package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(schedulerProvider: BaseSchedulerProvider,
                     cardRepository: CardRepository,
                     updateRepository: UpdateRepository) :
        BaseCardsPresenter<CardsContract.View>(schedulerProvider, cardRepository, updateRepository), CardsContract.Presenter

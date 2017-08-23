package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.ConnectionChecker
import com.jamieadkins.gwent.data.interactor.CardsInteractor

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(cardsInteractor: CardsInteractor, connectionChecker: ConnectionChecker) :
        BaseCardsPresenter<CardsContract.View>(cardsInteractor, connectionChecker), CardsContract.Presenter

package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.data.interactor.CardsInteractor

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(cardsInteractor: CardsInteractor) :
        BaseCardsPresenter<CardsContract.View>(cardsInteractor), CardsContract.Presenter

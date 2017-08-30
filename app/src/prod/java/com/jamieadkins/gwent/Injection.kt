package com.jamieadkins.gwent

import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase
import com.jamieadkins.gwent.data.interactor.CollectionInteractor
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase

object Injection {
    fun provideCardsInteractor(): CardsInteractor {
        return CardsInteractorFirebase.instance
    }

    fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }
}

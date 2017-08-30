package com.jamieadkins.gwent.data.interactor

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.deck.list.DecksContract

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Deck manipulation class.
 */

interface DeckListInteractor : BaseInteractor<DecksContract.Presenter> {

    fun createNewDeck(name: String, faction: String, leader: CardDetails, patch: String): Observable<RxDatabaseEvent<Deck>>

    val userDecks: Observable<RxDatabaseEvent<Deck>>

    val deckOfTheWeek: Single<RxDatabaseEvent<Deck>>

    val featuredDecks: Observable<RxDatabaseEvent<Deck>>

    fun stopData()
}

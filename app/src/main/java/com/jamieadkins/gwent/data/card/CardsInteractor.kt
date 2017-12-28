package com.jamieadkins.gwent.data.card

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.model.GwentCard

import io.reactivex.Flowable

/**
 * Card manipulation class.
 */

interface CardsInteractor : BaseInteractor {

    fun getAllCards(): Flowable<Collection<GwentCard>>

    fun getCards(filter: CardFilter): Flowable<Collection<GwentCard>>

    fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<Collection<GwentCard>>

    fun getCards(filter: CardFilter?, query: String?): Flowable<Collection<GwentCard>>

    fun getCard(id: String): Flowable<GwentCard>
}

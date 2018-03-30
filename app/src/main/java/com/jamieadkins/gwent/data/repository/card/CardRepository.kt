package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.GwentCard
import io.reactivex.Single

interface CardRepository {

    fun getCards(cardFilter: CardFilter? = null): Single<Collection<GwentCard>>

    fun getCards(cardIds: List<String>): Single<Collection<GwentCard>>

    fun searchCards(query: String): Single<Collection<GwentCard>>

    fun getCard(id: String): Single<GwentCard>
}
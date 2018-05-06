package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.CardDatabaseResult
import com.jamieadkins.gwent.domain.card.model.GwentCard
import io.reactivex.Single

interface CardRepository {

    fun getCards(cardFilter: CardFilter): Single<CardDatabaseResult>

    fun getCards(cardIds: List<String>): Single<Collection<GwentCard>>

    fun getCard(id: String): Single<GwentCard>
}
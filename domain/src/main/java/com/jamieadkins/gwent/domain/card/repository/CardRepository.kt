package com.jamieadkins.gwent.domain.card.repository

import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.GwentCard
import io.reactivex.Single

interface CardRepository {

    fun getCards(cardFilter: CardFilter): Single<CardDatabaseResult>

    fun getCards(cardIds: List<String>): Single<Collection<GwentCard>>

    fun getCard(id: String): Single<GwentCard>
}
package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.model.GwentCard
import com.jamieadkins.gwent.model.patch.PatchState
import io.reactivex.Completable
import io.reactivex.Single

interface CardRepository {

    fun checkForUpdates(): Single<PatchState>

    fun performUpdate(): Completable

    fun getCards(cardFilter: CardFilter? = null): Single<Collection<GwentCard>>

    fun getCards(cardIds: List<String>): Single<Collection<GwentCard>>

    fun searchCards(query: String): Single<Collection<GwentCard>>

    fun getCard(id: String): Single<GwentCard>
}
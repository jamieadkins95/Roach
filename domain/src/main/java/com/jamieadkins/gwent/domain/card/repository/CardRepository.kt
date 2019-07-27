package com.jamieadkins.gwent.domain.card.repository

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import io.reactivex.Observable

interface CardRepository {

    fun searchCards(searchQuery: String): Observable<List<GwentCard>>

    fun searchCardsInFactions(searchQuery: String, factions: List<GwentFaction>): Observable<List<GwentCard>>

    fun getCards(): Observable<List<GwentCard>>

    fun getCards(cardIds: List<String>): Observable<List<GwentCard>>

    fun getCard(id: String): Observable<GwentCard>

    fun getLeaders(faction: GwentFaction): Observable<List<GwentCard>>

    fun getCardsInFactions(factions: List<GwentFaction>): Observable<List<GwentCard>>

    fun invalidateMemoryCache()
}
package com.jamieadkins.gwent.data.repository.filter

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import io.reactivex.Observable

interface FilterRepository {

    fun getFilter(): Observable<CardFilter>

    fun updateSearchQuery(query: String)

    fun clearSearchQuery()

    fun updateSortParameter(sortedBy: SortedBy)

    fun updateFactionFilter(faction: GwentFaction, checked: Boolean)

    fun updateColourFilter(colour: GwentCardColour, checked: Boolean)

    fun updateRarityFilter(rarity: GwentCardRarity, checked: Boolean)

    fun setCollectibleOnly(collectibleOnly: Boolean)

    fun setDefaultFilters(rarities: List<GwentCardRarity> = GwentCardRarity.values().toList(),
                          factions: List<GwentFaction> = GwentFaction.values().toList(),
                          colours: List<GwentCardColour> = GwentCardColour.values().toList(),
                          collectibleOnly: Boolean = false)

    fun resetFilters()
}
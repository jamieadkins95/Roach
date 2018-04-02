package com.jamieadkins.gwent.data.repository.filter

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.card.SortedBy
import com.jamieadkins.gwent.core.GwentCardColour
import com.jamieadkins.gwent.core.GwentCardRarity
import com.jamieadkins.gwent.core.GwentFaction
import io.reactivex.Observable

interface FilterRepository {

    fun getFilter(): Observable<CardFilter>

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
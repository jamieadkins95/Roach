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

    fun setFactionEnabled(faction: GwentFaction, enabled: Boolean)

    fun setRarityEnabled(rarity: GwentCardRarity, enabled: Boolean)

    fun setColourEnabled(colour: GwentCardColour, enabled: Boolean)

    fun setCollectibleOnly(collectibleOnly: Boolean)
}
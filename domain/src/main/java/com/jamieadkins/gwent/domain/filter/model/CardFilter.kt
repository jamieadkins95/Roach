package com.jamieadkins.gwent.domain.filter.model

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.SortedBy

data class CardFilter(
        val searchQuery: String = "",
        val rarityFilter: Map<GwentCardRarity, Boolean>,
        val colourFilter: Map<GwentCardColour, Boolean>,
        val factionFilter: Map<GwentFaction, Boolean>,
        val isCollectibleOnly: Boolean = false,
        val sortedBy: SortedBy = SortedBy.ALPHABETICALLY_ASC)

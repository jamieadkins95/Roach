package com.jamieadkins.gwent.card

import com.jamieadkins.gwent.core.*

data class CardFilter(
        val rarityFilter: Map<GwentCardRarity, Boolean>,
        val colourFilter: Map<GwentCardColour, Boolean>,
        val factionFilter: Map<GwentFaction, Boolean>,
        val isCollectibleOnly: Boolean = false,
        val sortedBy: SortedBy = SortedBy.ALPHABETICALLY_ASC)

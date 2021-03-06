package com.jamieadkins.gwent.domain.filter.model

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentCardType
import com.jamieadkins.gwent.domain.card.model.GwentExpansion
import com.jamieadkins.gwent.domain.card.model.SortedBy

data class CardFilter(
    val rarityFilter: Map<GwentCardRarity, Boolean>,
    val colourFilter: Map<GwentCardColour, Boolean>,
    val factionFilter: Map<GwentFaction, Boolean>,
    val typeFilter: Map<GwentCardType, Boolean>,
    val expansionFilter: Map<GwentExpansion, Boolean>,
    val minProvisions: Int,
    val maxProvisions: Int,
    val isCollectibleOnly: Boolean,
    val sortedBy: SortedBy
)

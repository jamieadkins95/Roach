package com.jamieadkins.gwent.card

import com.jamieadkins.gwent.core.*

data class CardFilter(
        val rarityFilter: Map<GwentCardRarity, Boolean>,
        val colourFilter: Map<GwentCardColour, Boolean>,
        val factionFilter: Map<GwentFaction, Boolean>,
        val isCollectibleOnly: Boolean = false
) {

    fun doesCardMeetFilter(card: GwentCard): Boolean {
        val include = !isCollectibleOnly || card.collectible
        val faction = factionFilter[card.faction] ?: false
        val rarity = rarityFilter[card.rarity] ?: false
        val colour = colourFilter[card.colour] ?: false
        return (faction && rarity && colour && include)
    }
}

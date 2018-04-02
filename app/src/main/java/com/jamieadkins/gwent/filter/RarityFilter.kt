package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.core.GwentCardRarity

class RarityFilter(val rarity: GwentCardRarity, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return rarity.hashCode()
    }
}
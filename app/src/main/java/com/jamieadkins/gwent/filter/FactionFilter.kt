package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.core.GwentFaction

class FactionFilter(val faction: GwentFaction, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return faction.hashCode()
    }
}
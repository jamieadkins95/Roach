package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.domain.GwentFaction

class FactionFilter(val faction: GwentFaction, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return faction.hashCode()
    }
}
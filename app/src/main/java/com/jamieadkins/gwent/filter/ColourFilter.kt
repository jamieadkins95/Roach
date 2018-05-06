package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.domain.card.model.GwentCardColour

class ColourFilter(val colour: GwentCardColour, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return colour.hashCode()
    }
}
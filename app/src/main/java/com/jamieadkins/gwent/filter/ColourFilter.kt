package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.core.GwentCardColour

class ColourFilter(val colour: GwentCardColour, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return colour.hashCode()
    }
}
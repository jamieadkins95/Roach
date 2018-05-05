package com.jamieadkins.gwent.core

class GwentCard {
    var id: String? = null
    var tooltip: String? = null
    var flavor: String? = null
    var name: String? = null
    var categories = mutableListOf<String>()
    var loyalties = mutableListOf<GwentCardLoyalty>()
    var faction: GwentFaction? = null
    var strength: Int? = null
    var colour: GwentCardColour? = null
    var rarity: GwentCardRarity? = null
    var collectible = false

    var craftCost = 0
    var millValue = 0

    var relatedCards = listOf<String>()

    var cardArt: GwentCardArt? = null

    override fun toString(): String {
        return name ?: id ?: super.toString()
    }
}
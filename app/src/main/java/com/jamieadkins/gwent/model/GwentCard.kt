package com.jamieadkins.gwent.model

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter

class GwentCard : RecyclerViewItem {
    private val DEFAULT_LOCALE = "en-US"

    var id: String? = null
    var info = mutableMapOf<String, String>()
    var flavor = mutableMapOf<String, String>()
    var name = mutableMapOf<String, String>()
    var categories = listOf<String>()
    var loyalties = listOf<Loyalty>()
    var faction: GwentFaction? = null
    var strength: Int? = null
    var colour: CardColour? = null
    var rarity: Rarity? = null
    var collectible = false

    var craftValues = mutableMapOf<String, Int>()
    var millValues = mutableMapOf<String, Int>()

    var relatedCards = listOf<String>()

    var cardArt: CardArt? = null

    override fun getItemType(): Int {
        return if (colour == CardColour.LEADER) {
            GwentRecyclerViewAdapter.TYPE_CARD_LEADER
        } else {
            GwentRecyclerViewAdapter.TYPE_CARD
        }
    }

    override fun areContentsTheSame(other: RecyclerViewItem): Boolean {
        return other is GwentCard && info[DEFAULT_LOCALE] == other.info[DEFAULT_LOCALE]
    }
}
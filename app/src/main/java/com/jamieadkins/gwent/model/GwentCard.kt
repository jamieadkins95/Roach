package com.jamieadkins.gwent.model

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter

class GwentCard : RecyclerViewItem {
    private val DEFAULT_LOCALE = "en-US"

    var id: String? = null
    var info = mapOf<String, String>()
    var flavor = mapOf<String, String>()
    var name = mapOf<String, String>()
    var categories = mutableListOf<String>()
    var loyalties = mutableListOf<Loyalty>()
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

    override fun equals(other: Any?): Boolean {
        return other is GwentCard && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerViewItem): Boolean {
        return other is GwentCard && info[DEFAULT_LOCALE] == other.info[DEFAULT_LOCALE]
    }

    override fun toString(): String {
        return name[DEFAULT_LOCALE] ?: id ?: super.toString()
    }
}
package com.jamieadkins.gwent.model

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter

class Card : RecyclerViewItem {
    private val DEFAULT_LOCALE = "en-US"

    var ingameId: String? = null
    var info = mutableMapOf<String, String>()
    var flavor = mutableMapOf<String, String>()
    var name = mutableMapOf<String, String>()
    var type: CardColour? = null
    var faction: Faction? = null

    override fun getItemType(): Int {
        return if (type == CardColour.LEADER) {
            GwentRecyclerViewAdapter.TYPE_CARD_LEADER
        } else {
            GwentRecyclerViewAdapter.TYPE_CARD
        }
    }

    override fun areContentsTheSame(other: RecyclerViewItem): Boolean {
        return other is Card && info[DEFAULT_LOCALE] == other.info[DEFAULT_LOCALE]
    }
}
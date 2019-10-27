package com.jamieadkins.gwent.deck.builder

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_header.*

data class DeckHeaderItem(
    private val id: String,
    private val name: String,
    private val cardCount: Int,
    private val unitCount: Int,
    private val provisionCost: Int,
    private val provisionAllowance: Int
) : Item(id.toLongOrNull() ?: id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_deck_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val resources = viewHolder.itemView.resources
        viewHolder.name.text = name
        viewHolder.cardCount.text = resources.getString(R.string.cards_in_deck, cardCount, 25)
        viewHolder.unitCount.text = resources.getString(R.string.units_in_deck, unitCount, 13)
        viewHolder.deckProvisionCost.text = "$provisionCost/$provisionAllowance"
    }
}
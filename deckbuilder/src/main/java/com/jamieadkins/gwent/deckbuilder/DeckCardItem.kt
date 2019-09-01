package com.jamieadkins.gwent.deckbuilder

import android.view.View
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_card.*

data class DeckCardItem(
    val card: GwentCard,
    val countInDeck: Int
) : Item(card.id.toLongOrNull() ?: card.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_deck_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = card.name
        viewHolder.tooltip.text = card.tooltip
        viewHolder.provisionCost.text = card.provisions.toString()
        viewHolder.count.text = "x$countInDeck"

        if (card.strength > 0) {
            viewHolder.strength.visibility = View.VISIBLE
            viewHolder.strength.text = card.strength.toString()
        } else {
            viewHolder.strength.visibility = View.INVISIBLE
        }
    }
}
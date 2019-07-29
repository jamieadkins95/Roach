package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_search_result.*

data class SimpleGwentCardItem(
    val card: GwentCard
): Item(card.id.toLongOrNull() ?: card.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_search_result

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = card.name
        viewHolder.tooltip.text = card.tooltip
        viewHolder.provisions.text = card.provisions.toString()
    }
}
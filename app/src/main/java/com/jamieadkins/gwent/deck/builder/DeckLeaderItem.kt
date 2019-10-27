package com.jamieadkins.gwent.deck.builder

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_leader.*

data class DeckLeaderItem(
    val leader: GwentCard
) : Item(leader.id.toLongOrNull() ?: leader.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_deck_leader

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = leader.name
        viewHolder.tooltip.text = leader.tooltip
    }
}
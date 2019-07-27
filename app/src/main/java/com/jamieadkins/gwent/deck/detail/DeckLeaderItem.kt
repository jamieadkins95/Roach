package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_card.view.*
import kotlinx.android.synthetic.main.view_deck_leader.*

data class DeckLeaderItem(
    val leader: GwentCard
) : Item() {

    override fun getLayout(): Int = R.layout.view_deck_leader

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = leader.name
        viewHolder.tooltip.text = leader.tooltip
    }
}
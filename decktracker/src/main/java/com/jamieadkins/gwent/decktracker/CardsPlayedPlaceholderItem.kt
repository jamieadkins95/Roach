package com.jamieadkins.gwent.decktracker

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_analysis.*

class CardsPlayedPlaceholderItem: Item() {

    override fun getLayout(): Int = R.layout.view_cards_played_placeholder

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Do nothing
    }
}
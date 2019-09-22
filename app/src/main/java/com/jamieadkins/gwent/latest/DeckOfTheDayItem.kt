package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.latest.DeckOfTheDay
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_of_the_day.*

data class DeckOfTheDayItem(
    val deck: DeckOfTheDay
): Item(deck.id.toLong()) {

    override fun getLayout(): Int = R.layout.view_deck_of_the_day

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = deck.name
        viewHolder.author.text = deck.author
    }
}
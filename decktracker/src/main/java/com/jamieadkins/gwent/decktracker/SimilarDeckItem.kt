package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.tracker.predictions.SimilarDeck
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_similar_deck.*

data class SimilarDeckItem(
    val deck: SimilarDeck
): Item(deck.id.toLongOrNull() ?: deck.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_similar_deck

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = deck.name
    }
}
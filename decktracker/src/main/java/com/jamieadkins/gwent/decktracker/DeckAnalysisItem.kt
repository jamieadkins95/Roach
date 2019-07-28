package com.jamieadkins.gwent.decktracker

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_analysis.*

class DeckAnalysisItem(
    private val provisionsRemaining: Int,
    private val averageProvisionsRemaining: Float
): Item(provisionsRemaining.toLong()) {

    override fun getLayout(): Int = R.layout.view_deck_analysis

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.provisionsRemaining.text = provisionsRemaining.toString()
        viewHolder.averageProvisionsRemaining.text = averageProvisionsRemaining.toString()
    }
}
package com.jamieadkins.gwent.decktracker

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class NoPredictionsItem: Item() {

    override fun getLayout(): Int = R.layout.view_no_predictions

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Do nothing
    }
}
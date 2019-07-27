package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class SpaceItem : Item() {

    override fun getLayout(): Int = R.layout.view_space

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Do nothing.
    }
}
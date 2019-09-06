package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class LatestDividerItem : Item() {

    override fun getLayout(): Int = R.layout.view_latest_divider

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Do nothing.
    }
}


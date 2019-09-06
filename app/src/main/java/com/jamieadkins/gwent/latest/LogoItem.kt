package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class LogoItem : Item() {

    override fun getLayout(): Int = R.layout.view_logo

    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Do nothing
    }
}
package com.jamieadkins.gwent.base.items

import com.jamieadkins.gwent.base.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_header.*

data class H2HeaderItem(
    private val text: Int,
    private val secondaryText: Int
): Item(text.toLong()) {

    override fun getLayout(): Int = R.layout.view_header_h2

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.header_primary_text.setText(text)
        viewHolder.header_secondary_text.setText(secondaryText)
    }
}


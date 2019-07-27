package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_header.*

data class HeaderItem(private val text: Int, private val secondaryText: String): Item(text.toLong()) {

    override fun getLayout(): Int = R.layout.view_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.header_primary_text.setText(text)
        viewHolder.header_secondary_text.text = secondaryText
    }
}


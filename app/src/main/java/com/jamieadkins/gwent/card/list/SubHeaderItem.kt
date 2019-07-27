package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_subheader.*

class SubHeaderItem(private val text: Int): Item(text.toLong()) {

    override fun getLayout(): Int = R.layout.view_subheader

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.subheader_text.setText(text)
    }
}
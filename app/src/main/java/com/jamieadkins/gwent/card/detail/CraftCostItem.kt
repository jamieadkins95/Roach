package com.jamieadkins.gwent.card.detail

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_craft.*

class CraftCostItem(private val value: Int): Item(value.toLong()) {

    override fun getLayout(): Int = R.layout.view_craft

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text.text = value.toString()
    }
}
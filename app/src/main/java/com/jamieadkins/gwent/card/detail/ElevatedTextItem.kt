package com.jamieadkins.gwent.card.detail

import android.graphics.Typeface
import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_elevated_text.*

data class ElevatedTextItem(private val text: String, private val typeface: Typeface = Typeface.DEFAULT): Item(text.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_elevated_text

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text.text = text
        viewHolder.text.typeface = typeface
    }
}
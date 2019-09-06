package com.jamieadkins.gwent.latest

import androidx.annotation.StringRes
import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_header_latest.*

data class LatestHeaderItem(
    @StringRes private val text: Int
): Item(text.toLong()) {

    override fun getLayout(): Int = R.layout.view_header_latest

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.header_primary_text.setText(text)
    }
}


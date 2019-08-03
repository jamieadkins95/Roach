package com.jamieadkins.gwent.base.items

import com.jamieadkins.gwent.base.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_notice.*

data class NoticeItem(
    private val notice: String
) : Item(notice.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_notice

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = notice
    }
}
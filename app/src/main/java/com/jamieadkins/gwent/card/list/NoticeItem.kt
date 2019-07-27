package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.update.model.Notice
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_notice.*

data class NoticeItem(private val notice: Notice): Item(notice.id) {

    override fun getLayout(): Int = R.layout.view_notice

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = notice.title
    }
}
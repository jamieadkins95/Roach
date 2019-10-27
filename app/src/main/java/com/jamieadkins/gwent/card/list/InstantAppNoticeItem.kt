package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_instant_app_notice.view.*

data class InstantAppNoticeItem(
    private val onInstallClick: () -> Unit
) : Item() {

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is InstantAppNoticeItem
    }

    override fun getLayout(): Int = R.layout.view_instant_app_notice

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.installButton.setOnClickListener { onInstallClick.invoke() }
    }
}
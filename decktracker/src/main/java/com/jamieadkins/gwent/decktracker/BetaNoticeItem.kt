package com.jamieadkins.gwent.decktracker

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_beta_notice.*

data class BetaNoticeItem(
    val onFeedbackClick: () -> Unit
) : Item() {

    override fun getLayout(): Int = R.layout.view_beta_notice

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.btnFeedback.setOnClickListener { onFeedbackClick.invoke() }
    }
}
package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_analysis.*
import kotlinx.android.synthetic.main.view_predicted_card.*

class PredictedCardItem(
    private val card: GwentCard,
    private val prediction: Int
): Item(card.id.toLongOrNull() ?: card.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_predicted_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = card.name
        viewHolder.tooltip.text = card.tooltip
        viewHolder.prediction.text = viewHolder.itemView.context.getString(R.string.percentage, prediction)
    }
}
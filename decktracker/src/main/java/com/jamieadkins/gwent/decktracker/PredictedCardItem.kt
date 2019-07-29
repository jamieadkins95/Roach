package com.jamieadkins.gwent.decktracker

import androidx.core.content.ContextCompat
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_deck_analysis.*
import kotlinx.android.synthetic.main.view_predicted_card.*

data class PredictedCardItem(
    private val card: GwentCard,
    private val prediction: Int
): Item(card.id.toLongOrNull() ?: card.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_predicted_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = card.name
        val colour = if (card.colour == GwentCardColour.GOLD) R.color.gold else R.color.text_primary
        viewHolder.name.setTextColor(ContextCompat.getColor(viewHolder.itemView.context, colour))
        viewHolder.tooltip.text = card.tooltip
        viewHolder.prediction.text = "$prediction%"
    }
}
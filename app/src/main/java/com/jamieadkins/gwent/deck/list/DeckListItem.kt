package com.jamieadkins.gwent.deck.list

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_gwent_deck.*

data class DeckListItem(
    val deck: GwentDeck
) : Item(deck.id.toLongOrNull() ?: deck.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_gwent_deck

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = deck.name
        viewHolder.cardCount.text = "${deck.totalCardCount}/25"
        viewHolder.deckUnitCount.text = "${deck.unitCount}/13"
        viewHolder.deckProvisionCost.text = "${deck.provisionCost}/${deck.maxProvisionCost}"

        viewHolder.leaderName.text = deck.leader.name

        val context = viewHolder.itemView.context
        val factionColor = when (deck.leader.faction) {
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monstersLight)
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealmsLight)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiataelLight)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skelligeLight)
            GwentFaction.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaardLight)
            GwentFaction.SYNDICATE -> ContextCompat.getColor(context, R.color.syndicateLight)
            else -> ContextCompat.getColor(context, R.color.neutral)
        }
        viewHolder.leaderName.setTextColor(factionColor)

        val cardImage = deck.leader.cardArt.medium
        if (cardImage != null) {
            Glide.with(context)
                .load(cardImage)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.image)
        } else {
            viewHolder.image.setImageDrawable(null)
        }
    }
}
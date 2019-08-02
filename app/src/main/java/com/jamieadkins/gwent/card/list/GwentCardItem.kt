package com.jamieadkins.gwent.card.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.main.GwentStringHelper
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_gwent_card.*

data class GwentCardItem(
    val card: GwentCard
): Item(card.id.toLongOrNull() ?: card.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.view_gwent_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = card.name
        viewHolder.tooltip.text = card.tooltip
        viewHolder.categories.visibility = if (card.categories.isNotEmpty()) View.VISIBLE else View.GONE
        viewHolder.categories.text = card.categories.joinToString()
        if (card.provisions > 0) {
            viewHolder.strength.text = card.provisions.toString()
            viewHolder.strength.visibility = View.VISIBLE
        } else {
            viewHolder.strength.visibility = View.GONE
        }

        setCardArt(viewHolder.card_image, card.cardArt.medium, card.faction)
        setFactionText(viewHolder.faction, card.faction)
        setCardRarity(viewHolder.rarity, card.rarity)

    }

    private fun setCardArt(view: ImageView, image: String?, faction: GwentFaction) {
        if (image != null) {
            Glide.with(view.context)
                .load(image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view)
        } else {
            view.setImageDrawable(null)
        }
    }

    private fun setFactionText(view: TextView, faction: GwentFaction) {
        val context = view.context
        view.text = GwentStringHelper.getFactionString(context, faction)
        val factionColor = when (faction) {
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monstersLight)
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealmsLight)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiataelLight)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skelligeLight)
            GwentFaction.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaardLight)
            GwentFaction.SYNDICATE -> ContextCompat.getColor(context, R.color.syndicateLight)
            else -> ContextCompat.getColor(context, R.color.neutral)
        }
        view.setTextColor(factionColor)
    }

    private fun setCardRarity(view: TextView, rarity: GwentCardRarity) {
        val context = view.context
        view.text = GwentStringHelper.getRarityString(context, rarity)
        val rarityColor = when (rarity) {
            GwentCardRarity.COMMON -> ContextCompat.getColor(context, R.color.common)
            GwentCardRarity.RARE -> ContextCompat.getColor(context, R.color.rare)
            GwentCardRarity.EPIC -> ContextCompat.getColor(context, R.color.epic)
            GwentCardRarity.LEGENDARY -> ContextCompat.getColor(context, R.color.legendary)
            else -> ContextCompat.getColor(context, R.color.common)
        }
        view.setTextColor(rarityColor)
    }
}
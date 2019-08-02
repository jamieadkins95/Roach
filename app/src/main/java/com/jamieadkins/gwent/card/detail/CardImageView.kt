package com.jamieadkins.gwent.card.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction

class CardImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun setCardImage(imageUrl: String?, faction: GwentFaction) {
        val cardBack = when (faction) {
            GwentFaction.NORTHERN_REALMS -> R.drawable.cardback_northern_realms
            GwentFaction.NILFGAARD -> R.drawable.cardback_nilfgaard
            GwentFaction.NEUTRAL -> R.drawable.cardback_neutral
            GwentFaction.SCOIATAEL -> R.drawable.cardback_scoiatel
            GwentFaction.MONSTER -> R.drawable.cardback_monster
            GwentFaction.SKELLIGE -> R.drawable.cardback_skellige
            else -> R.drawable.cardback_neutral
        }

        if (imageUrl != null) {
            Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .error(cardBack)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(this)
        } else {
            Glide.with(context)
                .load(cardBack)
                .into(this)
        }
    }
}
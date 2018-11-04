package com.jamieadkins.gwent.deck.list

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import kotlinx.android.synthetic.main.view_gwent_deck.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DeckView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gwent_deck, this, true)
    }

    @CallbackProp
    fun setClickListener(listener: View.OnClickListener?) {
        setOnClickListener(listener)
    }

    @TextProp
    fun setDeckName(text: CharSequence) {
        name.text = text
    }

    @ModelProp
    fun setLeader(leader: GwentCard) {

        leaderName.text = leader.name

        val factionColor = when (leader.faction) {
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monstersLight)
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealmsLight)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiataelLight)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skelligeLight)
            GwentFaction.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaardLight)
            else -> ContextCompat.getColor(context, R.color.neutral)
        }
        leaderName.setTextColor(factionColor)

        val cardImage = leader.cardArt.medium
        if (cardImage != null) {
            Glide.with(context)
                .load(cardImage)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(image)
        } else {
            image.setImageDrawable(null)
        }
    }
}
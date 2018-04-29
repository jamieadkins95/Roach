package com.jamieadkins.gwent.view.card

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jamieadkins.gwent.core.GwentCardRarity
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.core.GwentStringHelper
import com.jamieadkins.gwent.view.R
import kotterknife.bindView
import org.jetbrains.annotations.Nullable
import java.lang.Exception

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GwentCardView : CardView {

    private val tvName by bindView<TextView>(R.id.name)
    private val tvTooltip by bindView<TextView>(R.id.tooltip)
    private val tvCategories by bindView<TextView>(R.id.categories)
    private val tvStrength by bindView<TextView>(R.id.strength)
    private val tvRarity by bindView<TextView>(R.id.rarity)
    private val tvFaction by bindView<TextView>(R.id.faction)
    private val imgCard by bindView<ImageView>(R.id.card_image)
    private val progress by bindView<ProgressBar>(R.id.progress)

    constructor(context: Context) : super(context) { inflateView() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { inflateView() }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { inflateView() }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.view_gwent_card, this, true)
    }

    @CallbackProp
    fun setClickListener(listener: View.OnClickListener?) {
        setOnClickListener(listener)
    }

    @TextProp
    fun setCardName(text: CharSequence) {
        tvName.text = text
    }

    @TextProp
    fun setCardTooltip(text: CharSequence) {
        tvTooltip.text = text
    }

    @ModelProp
    fun setCardCategories(categories: List<String>) {
        var text = ""
        categories.forEachIndexed { index, category ->
            text += category
            if (index != categories.size - 1) {
                text += ", "
            }
        }

        tvCategories.text = text

        if (text.isNotEmpty()) {
            tvCategories.visibility = View.VISIBLE
        } else {
            tvCategories.visibility = View.GONE
        }
    }

    @ModelProp
    fun setCardImage(imageUrl: String?) {
        imgCard.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .fitCenter()
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            progress.visibility = View.GONE
                            imgCard.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            progress.visibility = View.GONE
                            imgCard.visibility = View.VISIBLE
                            return false
                        }

                    })
                    .into(imgCard)
        } else {
            imgCard.setImageDrawable(null)
            progress.visibility = View.GONE
            imgCard.visibility = View.VISIBLE
        }
    }

    @ModelProp
    fun setCardFaction(faction: GwentFaction) {
        tvFaction.text = GwentStringHelper.getFactionString(context, faction)
        val factionColor = when (faction) {
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monsters)
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealms)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiatael)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skellige)
            GwentFaction.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaard)
            else -> ContextCompat.getColor(context, R.color.neutral)
        }
        tvFaction.setTextColor(factionColor)
    }

    @ModelProp
    fun setCardRarity(rarity: GwentCardRarity) {
        tvRarity.text = GwentStringHelper.getRarityString(context, rarity)
        val rarityColor = when (rarity) {
            GwentCardRarity.COMMON -> ContextCompat.getColor(context, R.color.common)
            GwentCardRarity.RARE -> ContextCompat.getColor(context, R.color.rare)
            GwentCardRarity.EPIC -> ContextCompat.getColor(context, R.color.epic)
            GwentCardRarity.LEGENDARY -> ContextCompat.getColor(context, R.color.legendary)
            else -> ContextCompat.getColor(context, R.color.common)
        }
        tvRarity.setTextColor(rarityColor)
    }

    @ModelProp
    fun setCardStrength(strength: Int?) {
        if (strength ?: 0 > 0) {
            tvStrength.text = strength.toString()
            tvStrength.visibility = View.VISIBLE
        } else {
            tvStrength.visibility = View.GONE
        }

    }
}
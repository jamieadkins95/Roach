package com.jamieadkins.gwent.card.list

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp

import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.main.GwentStringHelper
import kotterknife.bindView
import java.lang.Exception

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GwentCardView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    private val tvName by bindView<TextView>(R.id.name)
    private val tvTooltip by bindView<TextView>(R.id.tooltip)
    private val tvCategories by bindView<TextView>(R.id.categories)
    private val tvStrength by bindView<TextView>(R.id.strength)
    private val tvRarity by bindView<TextView>(R.id.rarity)
    private val tvFaction by bindView<TextView>(R.id.faction)
    private val imgCard by bindView<ImageView>(R.id.card_image)

    init {
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
        val text = categories.joinToString()
        tvCategories.text = text

        if (text.isNotEmpty()) {
            tvCategories.visibility = View.VISIBLE
        } else {
            tvCategories.visibility = View.GONE
        }
    }

    @JvmField @ModelProp var cardImage: String? = null
    lateinit var faction: GwentFaction

    @AfterPropsSet
    fun handleImage() {
        val cardBack = when (faction) {
            GwentFaction.NORTHERN_REALMS -> R.drawable.cardback_northern_realms
            GwentFaction.NILFGAARD -> R.drawable.cardback_nilfgaard
            GwentFaction.NEUTRAL -> R.drawable.cardback_neutral
            GwentFaction.SCOIATAEL -> R.drawable.cardback_scoiatel
            GwentFaction.MONSTER -> R.drawable.cardback_monster
            GwentFaction.SKELLIGE -> R.drawable.cardback_skellige
        }

        if (cardImage != null) {
            Glide.with(context)
                .load(cardImage)
                .placeholder(cardBack)
                .error(cardBack)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgCard)
        } else {
            Glide.with(context)
                .load(cardBack)
                .into(imgCard)
        }
    }

    @ModelProp
    fun setCardFaction(cardFaction: GwentFaction) {
        faction = cardFaction

        tvFaction.text = GwentStringHelper.getFactionString(context, faction)
        val factionColor = when (faction) {
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monstersLight)
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealmsLight)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiataelLight)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skelligeLight)
            GwentFaction.NEUTRAL -> ContextCompat.getColor(context, R.color.neutral)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaardLight)
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
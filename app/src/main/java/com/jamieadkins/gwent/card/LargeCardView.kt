package com.jamieadkins.gwent.card

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.filter.GwentStringHelper
import com.jamieadkins.gwent.model.*

/**
 * Wrapper for our card detail view.
 */

open class LargeCardView : SimpleCardView {
    private var mCardDescription: TextView? = null
    private var mCardFaction: TextView? = null
    private var mCardRarity: TextView? = null
    private var mCardLoyalty: TextView? = null
    private var mCardStrength: TextView? = null
    private var mCardType: TextView? = null

    var imageView: ImageView? = null
        private set

    private var mImageUrl: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initialiseView() {
        super.initialiseView()
        mCardDescription = findViewById<View>(R.id.card_description) as TextView
        mCardFaction = findViewById<View>(R.id.card_faction) as TextView
        mCardRarity = findViewById<View>(R.id.card_rarity) as TextView
        mCardLoyalty = findViewById<View>(R.id.card_loyalty) as TextView
        mCardStrength = findViewById<View>(R.id.card_strength) as TextView
        mCardType = findViewById<View>(R.id.card_type) as TextView
        imageView = findViewById<View>(R.id.card_image) as ImageView
    }

    override fun inflateView() {
        View.inflate(context, R.layout.item_card_large, this)
    }

    override fun setCardDetails(card: GwentCard) {
        super.setCardDetails(card)
        setDescription(card.info[locale])

        setFaction(card.faction)
        setRarity(card.rarity)
        if (card.loyalties.isNotEmpty()) {
            setLoyalty(card.loyalties[0])
        }
        setColour(card.colour)

        setStrength(card.strength.toString())
        mImageUrl = card.cardArt?.medium
    }

    fun loadImage() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(imageView?.context)
        if (sharedPreferences.getBoolean(imageView!!.context.getString(R.string.pref_show_images_key), true)) {
            imageView?.visibility = View.VISIBLE

            if (mImageUrl != null) {
                Glide.with(context)
                        .load(mImageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(imageView)
            } else {
                imageView?.setImageDrawable(null)
            }
        }
    }

    private fun setColour(cardColour: CardColour?) {
        mCardType?.let {
            it.text = GwentStringHelper.getColourString(context, cardColour)
            val typeColor: Int
            when (cardColour) {
                CardColour.BRONZE -> typeColor = ContextCompat.getColor(it.context, R.color.bronze)
                CardColour.SILVER -> typeColor = ContextCompat.getColor(it.context, R.color.silver)
                CardColour.GOLD -> typeColor = ContextCompat.getColor(it.context, R.color.gold)
                CardColour.LEADER -> typeColor = ContextCompat.getColor(it.context, R.color.gold)
                else -> typeColor = ContextCompat.getColor(it.context, R.color.bronze)
            }
            it.setTextColor(typeColor)
        }
    }

    private fun setLoyalty(loyalty: Loyalty?) {
        if (loyalty != null) {
            mCardLoyalty?.text = loyalty.toString()
        } else {
            mCardLoyalty?.visibility = View.GONE
        }
    }

    private fun setDescription(description: String?) {
        mCardDescription?.text = description
    }

    private fun setStrength(strength: String) {
        mCardStrength?.text = strength
    }

    private fun setRarity(rarity: Rarity?) {
        mCardRarity?.let {
            it.text = GwentStringHelper.getRarityString(context, rarity)

            val rarityColor: Int
            when (rarity) {
                Rarity.COMMON -> rarityColor = ContextCompat.getColor(it.context, R.color.common)
                Rarity.RARE -> rarityColor = ContextCompat.getColor(it.context, R.color.rare)
                Rarity.EPIC -> rarityColor = ContextCompat.getColor(it.context, R.color.epic)
                Rarity.LEGENDARY -> rarityColor = ContextCompat.getColor(it.context, R.color.legendary)
                else -> rarityColor = ContextCompat.getColor(it.context, R.color.common)
            }
            it.setTextColor(rarityColor)
        }
    }

    private fun setFaction(faction: GwentFaction?) {
        mCardFaction?.let {
            it.text = GwentStringHelper.getFactionString(context, faction)
            val factionColor: Int
            when (faction) {
                GwentFaction.MONSTER -> factionColor = ContextCompat.getColor(it.context, R.color.monsters)
                GwentFaction.NORTHERN_REALMS -> factionColor = ContextCompat.getColor(it.context, R.color.northernRealms)
                GwentFaction.SCOIATAEL -> factionColor = ContextCompat.getColor(it.context, R.color.scoiatael)
                GwentFaction.SKELLIGE -> factionColor = ContextCompat.getColor(it.context, R.color.skellige)
                GwentFaction.NEUTRAL -> factionColor = ContextCompat.getColor(it.context, R.color.neutral)
                GwentFaction.NILFGAARD -> factionColor = ContextCompat.getColor(it.context, R.color.nilfgaard)
                else -> factionColor = ContextCompat.getColor(it.context, R.color.neutral)
            }
            it.setTextColor(factionColor)
        }
    }
}

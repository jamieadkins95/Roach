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
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Faction
import com.jamieadkins.gwent.data.Rarity
import com.jamieadkins.gwent.data.Type

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

    override fun setCardDetails(cardDetails: CardDetails) {
        super.setCardDetails(cardDetails)
        setDescription(cardDetails.getInfo(locale))
        setFaction(cardDetails.faction)
        setRarity(cardDetails.rarity)
        if (cardDetails.loyalties != null) {
            setLoyalty(cardDetails.loyalties[0])
        }
        if (cardDetails.type != null) {
            setType(cardDetails.type)
        }

        setStrength(cardDetails.strength.toString())
        mImageUrl = cardDetails.image
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

    private fun setType(type: String) {
        mCardType?.let {
            it.text = type
            val typeColor: Int
            when (type) {
                Type.BRONZE_ID -> typeColor = ContextCompat.getColor(it.context, R.color.bronze)
                Type.SILVER_ID -> typeColor = ContextCompat.getColor(it.context, R.color.silver)
                Type.GOLD_ID -> typeColor = ContextCompat.getColor(it.context, R.color.gold)
                Type.LEADER_ID -> typeColor = ContextCompat.getColor(it.context, R.color.gold)
                else -> typeColor = ContextCompat.getColor(it.context, R.color.common)
            }
            it.setTextColor(typeColor)
        }

    }

    private fun setLoyalty(loyalty: String?) {
        if (loyalty != null) {
            mCardLoyalty?.text = loyalty
        } else {
            mCardLoyalty?.visibility = View.GONE
        }
    }

    private fun setDescription(description: String) {
        mCardDescription?.text = description
    }

    private fun setStrength(strength: String) {
        mCardStrength?.text = strength
    }

    private fun setRarity(rarity: String) {
        mCardRarity?.let {
            it.text = rarity

            val rarityColor: Int
            when (rarity) {
                Rarity.COMMON_ID -> rarityColor = ContextCompat.getColor(it.context, R.color.common)
                Rarity.RARE_ID -> rarityColor = ContextCompat.getColor(it.context, R.color.rare)
                Rarity.EPIC_ID -> rarityColor = ContextCompat.getColor(it.context, R.color.epic)
                Rarity.LEGENDARY_ID -> rarityColor = ContextCompat.getColor(it.context, R.color.legendary)
                else -> rarityColor = ContextCompat.getColor(it.context, R.color.common)
            }
            it.setTextColor(rarityColor)
        }
    }

    private fun setFaction(faction: String) {
        mCardFaction?.let {
            it.text = faction
            val factionColor: Int
            when (faction) {
                Faction.MONSTERS_ID -> factionColor = ContextCompat.getColor(it.context, R.color.monsters)
                Faction.NORTHERN_REALMS_ID -> factionColor = ContextCompat.getColor(it.context, R.color.northernRealms)
                Faction.SCOIATAEL_ID -> factionColor = ContextCompat.getColor(it.context, R.color.scoiatael)
                Faction.SKELLIGE_ID -> factionColor = ContextCompat.getColor(it.context, R.color.skellige)
                Faction.NEUTRAL_ID -> factionColor = ContextCompat.getColor(it.context, R.color.neutral)
                Faction.NILFGAARD_ID -> factionColor = ContextCompat.getColor(it.context, R.color.nilfgaard)
                else -> factionColor = ContextCompat.getColor(it.context, R.color.neutral)
            }
            it.setTextColor(factionColor)
        }
    }
}

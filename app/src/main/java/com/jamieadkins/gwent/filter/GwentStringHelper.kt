package com.jamieadkins.gwent.filter

import android.content.Context
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.Loyalty
import com.jamieadkins.gwent.model.Rarity

object GwentStringHelper {

    fun getRarityString(context: Context, rarity: Rarity?): String? {
        return when(rarity) {
            Rarity.COMMON -> context.getString(R.string.common)
            Rarity.RARE -> context.getString(R.string.rare)
            Rarity.EPIC -> context.getString(R.string.epic)
            Rarity.LEGENDARY -> context.getString(R.string.legendary)
            else -> null
        }
    }

    fun getColourString(context: Context, colour: CardColour?): String? {
        return when(colour) {
            CardColour.BRONZE -> context.getString(R.string.bronze)
            CardColour.SILVER -> context.getString(R.string.silver)
            CardColour.GOLD -> context.getString(R.string.gold)
            CardColour.LEADER -> context.getString(R.string.leader)
            else -> null
        }
    }

    fun getFactionString(context: Context, faction: GwentFaction?): String? {
        return when(faction) {
            GwentFaction.MONSTER -> context.getString(R.string.monster)
            GwentFaction.NORTHERN_REALMS -> context.getString(R.string.northern_realms)
            GwentFaction.SCOIATAEL -> context.getString(R.string.scoiatael)
            GwentFaction.SKELLIGE -> context.getString(R.string.skellige)
            GwentFaction.NILFGAARD -> context.getString(R.string.nilfgaard)
            GwentFaction.NEUTRAL -> context.getString(R.string.neutral)
            else -> null
        }
    }

    fun getLoyaltyString(context: Context, colour: Loyalty?): String? {
        return when(colour) {
            Loyalty.LOYAL -> context.getString(R.string.loyal)
            Loyalty.DISLOYAL -> context.getString(R.string.disloyal)
            else -> null
        }
    }
}
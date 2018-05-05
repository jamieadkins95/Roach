package com.jamieadkins.gwent.card

import android.content.Context
import android.support.v4.content.ContextCompat
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.core.GwentFaction

object CardResourceHelper {

    fun getColorForFaction(context: Context, faction: GwentFaction): Int {
        return when (faction) {
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealms)
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monsters)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiatael)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skellige)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaard)
            else -> ContextCompat.getColor(context, R.color.gwentGreen)
        }
    }

    fun getDarkColorForFaction(context: Context, faction: GwentFaction): Int {
        return when (faction) {
            GwentFaction.NORTHERN_REALMS -> ContextCompat.getColor(context, R.color.northernRealmsDark)
            GwentFaction.MONSTER -> ContextCompat.getColor(context, R.color.monstersDark)
            GwentFaction.SCOIATAEL -> ContextCompat.getColor(context, R.color.scoiataelDark)
            GwentFaction.SKELLIGE -> ContextCompat.getColor(context, R.color.skelligeDark)
            GwentFaction.NILFGAARD -> ContextCompat.getColor(context, R.color.nilfgaardDark)
            else -> ContextCompat.getColor(context, R.color.gwentGreenDark)
        }
    }
}
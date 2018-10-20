package com.jamieadkins.gwent.main

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.R

object CardResourceHelper {

    fun getColorForFaction(resources: Resources, faction: GwentFaction): Int {
        return when (faction) {
            GwentFaction.NORTHERN_REALMS -> ResourcesCompat.getColor(resources, R.color.northernRealms, null)
            GwentFaction.MONSTER -> ResourcesCompat.getColor(resources, R.color.monsters, null)
            GwentFaction.SCOIATAEL -> ResourcesCompat.getColor(resources, R.color.scoiatael, null)
            GwentFaction.SKELLIGE -> ResourcesCompat.getColor(resources, R.color.skellige, null)
            GwentFaction.NILFGAARD -> ResourcesCompat.getColor(resources, R.color.nilfgaard, null)
            else -> ResourcesCompat.getColor(resources, R.color.gwentGreen, null)
        }
    }

    fun getDarkColorForFaction(resources: Resources, faction: GwentFaction): Int {
        return when (faction) {
            GwentFaction.NORTHERN_REALMS -> ResourcesCompat.getColor(resources, R.color.northernRealmsDark, null)
            GwentFaction.MONSTER -> ResourcesCompat.getColor(resources, R.color.monstersDark, null)
            GwentFaction.SCOIATAEL -> ResourcesCompat.getColor(resources, R.color.scoiataelDark, null)
            GwentFaction.SKELLIGE -> ResourcesCompat.getColor(resources, R.color.skelligeDark, null)
            GwentFaction.NILFGAARD -> ResourcesCompat.getColor(resources, R.color.nilfgaardDark, null)
            else -> ResourcesCompat.getColor(resources, R.color.gwentGreenDark, null)
        }
    }
}
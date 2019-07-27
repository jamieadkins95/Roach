package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.card.data.model.Faction
import com.jamieadkins.gwent.domain.GwentFaction
import javax.inject.Inject

class FactionMapper @Inject constructor() {

    fun map(from: String): GwentFaction {
        return when (from) {
            Faction.MONSTERS_ID -> GwentFaction.MONSTER
            Faction.NORTHERN_REALMS_ID -> GwentFaction.NORTHERN_REALMS
            Faction.SCOIATAEL_ID -> GwentFaction.SCOIATAEL
            Faction.SKELLIGE_ID -> GwentFaction.SKELLIGE
            Faction.NILFGAARD_ID -> GwentFaction.NILFGAARD
            Faction.NEUTRAL_ID -> GwentFaction.NEUTRAL
            Faction.SYNDICATE_ID -> GwentFaction.SYNDICATE
            else -> throw Exception("Faction not found")
        }
    }
}
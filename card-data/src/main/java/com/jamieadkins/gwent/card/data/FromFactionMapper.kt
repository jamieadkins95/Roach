package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.card.data.model.Faction
import com.jamieadkins.gwent.domain.GwentFaction
import javax.inject.Inject

class FromFactionMapper @Inject constructor() {

    fun map(from: GwentFaction): String {
        return when (from) {
            GwentFaction.MONSTER -> Faction.MONSTERS_ID
            GwentFaction.NORTHERN_REALMS -> Faction.NORTHERN_REALMS_ID
            GwentFaction.SCOIATAEL -> Faction.SCOIATAEL_ID
            GwentFaction.SKELLIGE -> Faction.SKELLIGE_ID
            GwentFaction.NILFGAARD -> Faction.NILFGAARD_ID
            GwentFaction.NEUTRAL -> Faction.NEUTRAL_ID
            GwentFaction.SYNDICATE -> Faction.SYNDICATE_ID
        }
    }
}
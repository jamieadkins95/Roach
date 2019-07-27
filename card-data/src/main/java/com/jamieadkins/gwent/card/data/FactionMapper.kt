package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.card.data.model.Faction
import com.jamieadkins.gwent.domain.GwentFaction
import javax.inject.Inject

class FactionMapper @Inject constructor() : Mapper<String, GwentFaction>() {

    override fun map(from: String): GwentFaction {
        return when (from) {
            com.jamieadkins.gwent.card.data.model.Faction.MONSTERS_ID -> GwentFaction.MONSTER
            com.jamieadkins.gwent.card.data.model.Faction.NORTHERN_REALMS_ID -> GwentFaction.NORTHERN_REALMS
            com.jamieadkins.gwent.card.data.model.Faction.SCOIATAEL_ID -> GwentFaction.SCOIATAEL
            com.jamieadkins.gwent.card.data.model.Faction.SKELLIGE_ID -> GwentFaction.SKELLIGE
            com.jamieadkins.gwent.card.data.model.Faction.NILFGAARD_ID -> GwentFaction.NILFGAARD
            com.jamieadkins.gwent.card.data.model.Faction.NEUTRAL_ID -> GwentFaction.NEUTRAL
            com.jamieadkins.gwent.card.data.model.Faction.SYNDICATE_ID -> GwentFaction.SYNDICATE
            else -> throw Exception("Faction not found")
        }
    }
}
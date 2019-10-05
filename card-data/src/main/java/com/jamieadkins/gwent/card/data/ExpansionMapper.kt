package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.card.data.model.Expansion
import com.jamieadkins.gwent.card.data.model.Faction
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentExpansion
import javax.inject.Inject

class ExpansionMapper @Inject constructor() {

    fun map(from: String): GwentExpansion {
        return when (from) {
            Expansion.BASE_ID -> GwentExpansion.Base
            Expansion.UNMILLABLE_ID -> GwentExpansion.Unmillable
            Expansion.TOKEN_ID -> GwentExpansion.Token
            Expansion.THRONEBREAKER_ID -> GwentExpansion.Thronebreaker
            Expansion.CRIMSONCURSE_ID -> GwentExpansion.CrimsonCurse
            Expansion.NOVIGRAD_ID -> GwentExpansion.Novigrad
            Expansion.IRON_JUDGEMENT_ID -> GwentExpansion.IronJudgement
            else -> GwentExpansion.Base
        }
    }
}
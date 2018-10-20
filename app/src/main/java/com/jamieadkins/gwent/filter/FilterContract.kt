package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity

interface FilterContract {
    interface View {

        fun showFactions(factions: Map<GwentFaction, Boolean>)

        fun showTiers(tiers: Map<GwentCardColour, Boolean>)

        fun showRarities(rarities: Map<GwentCardRarity, Boolean>)

        fun showProvisions(provisions: Map<Int, Boolean>)
    }

    interface Presenter
}

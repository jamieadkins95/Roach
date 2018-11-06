package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.main.MvpPresenter

interface FilterContract {
    interface View {

        fun close()

        fun setNilfgaardFilter(checked: Boolean)

        fun setNorthernRealmsFilter(checked: Boolean)

        fun setMonsterFilter(checked: Boolean)

        fun setSkelligeFilter(checked: Boolean)

        fun setScoiataelFilter(checked: Boolean)

        fun setNeutralFilter(checked: Boolean)

        fun setBronzeFilter(checked: Boolean)

        fun setGoldFilter(checked: Boolean)

        fun setLeaderFilter(checked: Boolean)

        fun setCommonFilter(checked: Boolean)

        fun setRareFilter(checked: Boolean)

        fun setEpicFilter(checked: Boolean)

        fun setLegendaryFilter(checked: Boolean)

        fun setMinProvisions(provisions: Int)

        fun setMaxProvisions(provisions: Int)

        fun setUnitFilter(checked: Boolean)

        fun setArtifactFilter(checked: Boolean)

        fun setSpellFilter(checked: Boolean)

        fun setTypeLeaderFilter(checked: Boolean)

        fun showFiltersForDeckBuilder(faction: GwentFaction)
    }

    interface Presenter : MvpPresenter {

        fun setDeck(deckId: String)

        fun applyFilters()

        fun resetFilters()

        fun onNilfgaardFilterChanged(checked: Boolean)

        fun onNorthernRealmsFilterChanged(checked: Boolean)

        fun onMonsterFilterChanged(checked: Boolean)

        fun onSkelligeFilterChanged(checked: Boolean)

        fun onScoiataelFilterChanged(checked: Boolean)

        fun onNeutralFilterChanged(checked: Boolean)

        fun onBronzeChanged(checked: Boolean)

        fun onGoldChanged(checked: Boolean)

        fun onLeaderChanged(checked: Boolean)

        fun onCommonChanged(checked: Boolean)

        fun onRareChanged(checked: Boolean)

        fun onEpicChanged(checked: Boolean)

        fun onLegendaryChanged(checked: Boolean)

        fun onMinProvisionsChanged(min: Int)

        fun onMaxProvisionsChanged(max: Int)

        fun onTypeUnitChanged(checked: Boolean)

        fun onTypeSpellChanged(checked: Boolean)

        fun onTypeArtifactChanged(checked: Boolean)

        fun onTypeLeaderChanged(checked: Boolean)
    }
}

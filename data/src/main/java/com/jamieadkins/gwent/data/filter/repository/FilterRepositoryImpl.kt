package com.jamieadkins.gwent.data.filter.repository

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor() : FilterRepository {

    private val rarityFilter = mutableMapOf<GwentCardRarity, Boolean>()
    private val colourFilter = mutableMapOf<GwentCardColour, Boolean>()
    private val factionFilter = mutableMapOf<GwentFaction, Boolean>()
    private var sortOrder = SortedBy.ALPHABETICALLY_ASC
    private var isCollectibleOnly = false

    private var defaultCollectibleOnly = false

    private val filter = BehaviorSubject.createDefault(
        CardFilter(
            "",
            GwentCardRarity.values().map { it to true }.toMap().toMutableMap(),
            GwentCardColour.values().map { it to true }.toMap().toMutableMap(),
            GwentFaction.values().map { it to true }.toMap().toMutableMap(),
            false,
            SortedBy.ALPHABETICALLY_ASC)
    )

    override fun getFilter(): Observable<CardFilter> = filter

    override fun updateSortParameter(sortedBy: SortedBy) {

        // Do nothing.
    }

    override fun updateFactionFilter(faction: GwentFaction, checked: Boolean) {

        // Do nothing.
    }

    override fun updateColourFilter(colour: GwentCardColour, checked: Boolean) {

        // Do nothing.
    }

    override fun updateRarityFilter(rarity: GwentCardRarity, checked: Boolean) {

        // Do nothing.
    }

    override fun setCollectibleOnly(collectibleOnly: Boolean) {

        // Do nothing.
    }

    override fun setDefaultFilters(rarities: List<GwentCardRarity>,
                                   factions: List<GwentFaction>,
                                   colours: List<GwentCardColour>,
                                   collectibleOnly: Boolean) {

        // Do nothing.
    }

    override fun resetFilters() {

        // Do nothing.
    }

    override fun updateSearchQuery(query: String) {

        // Do nothing.
    }

    override fun clearSearchQuery() {
        // Do nothing.
    }
}
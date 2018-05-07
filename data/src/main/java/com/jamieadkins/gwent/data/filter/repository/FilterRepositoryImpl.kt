package com.jamieadkins.gwent.data.filter.repository

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FilterRepositoryImpl : FilterRepository {
    private val filter = BehaviorSubject.create<CardFilter>()

    private val rarityFilter = mutableMapOf<GwentCardRarity, Boolean>()
    private val colourFilter = mutableMapOf<GwentCardColour, Boolean>()
    private val factionFilter = mutableMapOf<GwentFaction, Boolean>()
    private var sortOrder = SortedBy.ALPHABETICALLY_ASC
    private var isCollectibleOnly = false

    private var defaultCollectibleOnly = false

    private var searchQuery: String = ""

    override fun getFilter(): Observable<CardFilter> = filter

    override fun updateSortParameter(sortedBy: SortedBy) {
        sortOrder = sortedBy
        filter.onNext(createCardFilter())
    }

    override fun updateFactionFilter(faction: GwentFaction, checked: Boolean) {
        factionFilter[faction] = checked
        filter.onNext(createCardFilter())
    }

    override fun updateColourFilter(colour: GwentCardColour, checked: Boolean) {
        colourFilter[colour] = checked
        filter.onNext(createCardFilter())
    }

    override fun updateRarityFilter(rarity: GwentCardRarity, checked: Boolean) {
        rarityFilter[rarity] = checked
        filter.onNext(createCardFilter())
    }

    override fun setCollectibleOnly(collectibleOnly: Boolean) {
        isCollectibleOnly = collectibleOnly
        filter.onNext(createCardFilter())
    }

    override fun setDefaultFilters(rarities: List<GwentCardRarity>,
                                   factions: List<GwentFaction>,
                                   colours: List<GwentCardColour>,
                                   collectibleOnly: Boolean) {
        searchQuery = ""

        rarityFilter.clear()
        rarities.forEach {
            rarityFilter[it] = true
        }

        factionFilter.clear()
        factions.forEach {
            factionFilter[it] = true
        }

        colourFilter.clear()
        colours.forEach {
            colourFilter[it] = true
        }

        defaultCollectibleOnly = collectibleOnly
        isCollectibleOnly = collectibleOnly

        filter.onNext(createCardFilter())
    }

    override fun resetFilters() {
        rarityFilter.keys.forEach {
            rarityFilter[it] = true
        }

        factionFilter.keys.forEach {
            factionFilter[it] = true
        }

        colourFilter.keys.forEach {
            colourFilter[it] = true
        }

        isCollectibleOnly = defaultCollectibleOnly

        filter.onNext(createCardFilter())
    }

    override fun updateSearchQuery(query: String) {
        if (query.isEmpty()) {
            clearSearchQuery()
        } else {
            searchQuery = query
            sortOrder = SortedBy.SEARCH_RELEVANCE
            filter.onNext(createCardFilter())
        }
    }

    override fun clearSearchQuery() {
        searchQuery = ""
        sortOrder = SortedBy.ALPHABETICALLY_ASC
        filter.onNext(createCardFilter())
    }

    private fun createCardFilter(): CardFilter {
        return CardFilter(searchQuery, rarityFilter, colourFilter, factionFilter, isCollectibleOnly)
    }
}
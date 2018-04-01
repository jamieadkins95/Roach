package com.jamieadkins.gwent.data.repository.filter

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.card.SortedBy
import com.jamieadkins.gwent.core.GwentCardColour
import com.jamieadkins.gwent.core.GwentCardRarity
import com.jamieadkins.gwent.core.GwentFaction
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FilterRepositoryImpl : FilterRepository {
    private val sort = BehaviorSubject.create<SortedBy>()
            // Default to sorted Alphabetically.
            .apply { startWith(SortedBy.ALPHABETICALLY_ASC) }

    private val filter = BehaviorSubject.create<CardFilter>()

    private val rarityFilter = mutableMapOf<GwentCardRarity, Boolean>()
    private val colourFilter = mutableMapOf<GwentCardColour, Boolean>()
    private val factionFilter = mutableMapOf<GwentFaction, Boolean>()

    private var isCollectibleOnly = false

    init {
        for (rarity in GwentCardRarity.values()) {
            rarityFilter[rarity] = true
        }

        for (colour in GwentCardColour.values()) {
            colourFilter[colour] = true
        }

        for (faction in GwentFaction.values()) {
            factionFilter[faction] = true
        }

        filter.onNext(createCardFilter())
    }

    override fun getSortParameter(): Observable<SortedBy> = sort

    override fun getFilter(): Observable<CardFilter> = filter

    override fun updateSortParameter(sortedBy: SortedBy) {
        sort.onNext(sortedBy)
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

    override fun setFactionEnabled(faction: GwentFaction, enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRarityEnabled(rarity: GwentCardRarity, enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setColourEnabled(colour: GwentCardColour, enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCollectibleOnly(collectibleOnly: Boolean) {
        isCollectibleOnly = collectibleOnly
        filter.onNext(createCardFilter())
    }

    private fun createCardFilter(): CardFilter {
        return CardFilter(rarityFilter, colourFilter, factionFilter, isCollectibleOnly)
    }
}
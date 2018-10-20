package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.filter.GetFilterUseCase
import com.jamieadkins.gwent.domain.filter.SetFilterUseCase
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.main.BasePresenter
import javax.inject.Inject

class FilterPresenter @Inject constructor(
    private val view: FilterContract.View,
    private val getFilterUseCase: GetFilterUseCase,
    private val setFilterUseCase: SetFilterUseCase
) : BasePresenter(), FilterContract.Presenter {

    var nilfgaard = false
    var northernRealms = false
    var monster = false
    var skellige = false
    var scoiatael = false
    var neutral = false

    var bronze = false
    var gold = false
    var leader = false

    override fun onAttach() {
        getFilterUseCase.getFilter()
            .firstOrError()
            .subscribeWith(object : BaseDisposableSingle<CardFilter>() {
                override fun onSuccess(filter: CardFilter) {
                    nilfgaard = filter.factionFilter[GwentFaction.NILFGAARD] ?: false
                    view.setNilfgaardFilter(nilfgaard)

                    northernRealms = filter.factionFilter[GwentFaction.NORTHERN_REALMS] ?: false
                    view.setNorthernRealmsFilter(northernRealms)

                    monster = filter.factionFilter[GwentFaction.MONSTER] ?: false
                    view.setMonsterFilter(monster)

                    skellige = filter.factionFilter[GwentFaction.SKELLIGE] ?: false
                    view.setSkelligeFilter(skellige)

                    scoiatael = filter.factionFilter[GwentFaction.SCOIATAEL] ?: false
                    view.setScoiataelFilter(scoiatael)

                    neutral = filter.factionFilter[GwentFaction.NEUTRAL] ?: false
                    view.setNeutralFilter(neutral)

                    bronze = filter.colourFilter[GwentCardColour.BRONZE] ?: false
                    view.setBronzeFilter(bronze)

                    gold = filter.colourFilter[GwentCardColour.GOLD] ?: false
                    view.setGoldFilter(gold)

                    leader = filter.colourFilter[GwentCardColour.LEADER] ?: false
                    view.setLeaderFilter(leader)
                }
            })
            .addToComposite()
    }

    override fun resetFilters() {
        setFilterUseCase.setFilter(CardFilter())
        view.close()
    }

    override fun applyFilters() {
        val factions = mapOf(
            GwentFaction.NILFGAARD to nilfgaard,
            GwentFaction.NORTHERN_REALMS to northernRealms,
            GwentFaction.MONSTER to monster,
            GwentFaction.SKELLIGE to skellige,
            GwentFaction.SCOIATAEL to scoiatael,
            GwentFaction.NEUTRAL to neutral
        )

        val colours = mapOf(
            GwentCardColour.BRONZE to bronze,
            GwentCardColour.GOLD to gold,
            GwentCardColour.LEADER to leader
        )
        setFilterUseCase.setFilter(CardFilter(colourFilter = colours, factionFilter = factions))
        view.close()
    }

    override fun onNilfgaardFilterChanged(checked: Boolean) {
        nilfgaard = checked
    }

    override fun onNorthernRealmsFilterChanged(checked: Boolean) {
        northernRealms = checked
    }

    override fun onMonsterFilterChanged(checked: Boolean) {
        monster = checked
    }

    override fun onSkelligeFilterChanged(checked: Boolean) {
        skellige = checked
    }

    override fun onScoiataelFilterChanged(checked: Boolean) {
        scoiatael = checked
    }

    override fun onNeutralFilterChanged(checked: Boolean) {
        neutral = checked
    }

    override fun onBronzeChanged(checked: Boolean) {
        bronze = checked
    }

    override fun onGoldChanged(checked: Boolean) {
        gold = checked
    }

    override fun onLeaderChanged(checked: Boolean) {
        leader = checked
    }

    override fun onRefresh() {
        // Do nothing
    }
}
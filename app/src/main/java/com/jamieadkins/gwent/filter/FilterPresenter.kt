package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.domain.GwentFaction
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

    override fun onAttach() {
        getFilterUseCase.getFilter()
            .subscribeWith(object : BaseDisposableObserver<CardFilter>() {
                override fun onNext(filter: CardFilter) {
                    val ng = filter.factionFilter[GwentFaction.NILFGAARD] ?: false
                    nilfgaard = ng
                    view.setNilfgaardFilter(nilfgaard)

                    val nr = filter.factionFilter[GwentFaction.NORTHERN_REALMS] ?: false
                    northernRealms = nr
                    view.setNorthernRealmsFilter(northernRealms)

                    val mn = filter.factionFilter[GwentFaction.MONSTER] ?: false
                    monster = mn
                    view.setMonsterFilter(monster)

                    val sk = filter.factionFilter[GwentFaction.SKELLIGE] ?: false
                    skellige = sk
                    view.setSkelligeFilter(skellige)

                    val sc = filter.factionFilter[GwentFaction.SCOIATAEL] ?: false
                    scoiatael = sc
                    view.setScoiataelFilter(scoiatael)

                    val ne = filter.factionFilter[GwentFaction.NEUTRAL] ?: false
                    neutral = ne
                    view.setNeutralFilter(neutral)
                }
            })
            .addToComposite()
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
        setFilterUseCase.setFilter(CardFilter(factionFilter = factions))
        view.close()
    }

    override fun onNilfgaardFilterChanged(checked: Boolean) { nilfgaard = checked }

    override fun onNorthernRealmsFilterChanged(checked: Boolean) { northernRealms = checked }

    override fun onMonsterFilterChanged(checked: Boolean) { monster = checked }

    override fun onSkelligeFilterChanged(checked: Boolean) { skellige = checked }

    override fun onScoiataelFilterChanged(checked: Boolean) { scoiatael = checked }

    override fun onNeutralFilterChanged(checked: Boolean) { neutral = checked }

    override fun onRefresh() {
        // Do nothing
    }
}
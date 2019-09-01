package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentCardType
import com.jamieadkins.gwent.domain.deck.GetDeckUseCase
import com.jamieadkins.gwent.domain.filter.GetFilterUseCase
import com.jamieadkins.gwent.domain.filter.ResetFilterUseCase
import com.jamieadkins.gwent.domain.filter.SetFilterUseCase
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.base.BasePresenter
import com.jamieadkins.gwent.domain.card.model.GwentExpansion
import com.jamieadkins.gwent.domain.card.model.SortedBy
import javax.inject.Inject

class FilterPresenter @Inject constructor(
    private val view: FilterContract.View,
    private val getFilterUseCase: GetFilterUseCase,
    private val setFilterUseCase: SetFilterUseCase,
    private val resetFilterUseCase: ResetFilterUseCase,
    private val getDeckUseCase: GetDeckUseCase
) : BasePresenter(), FilterContract.Presenter {

    var nilfgaard = false
    var northernRealms = false
    var monster = false
    var skellige = false
    var scoiatael = false
    var neutral = false
    var syndicate = false

    var bronze = false
    var gold = false
    var leader = false

    var common = false
    var rare = false
    var epic = false
    var legendary = false

    var unit = false
    var spell = false
    var artifact = false

    var baseSet = false
    var tokenSet = false
    var unmillableSet = false
    var thronebreaker = false
    var crimsonCurse = false
    var novigrad = false

    var minProvisions = 0
    var maxProvisions = 100

    var sortedBy = SortedBy.ALPHABETICALLY_ASC

    private var deckId: String = ""

    override fun onAttach() {
        getFilterUseCase.getFilter(deckId)
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

                    syndicate = filter.factionFilter[GwentFaction.SYNDICATE] ?: false
                    view.setSyndicateFilter(syndicate)

                    bronze = filter.colourFilter[GwentCardColour.BRONZE] ?: false
                    view.setBronzeFilter(bronze)

                    gold = filter.colourFilter[GwentCardColour.GOLD] ?: false
                    view.setGoldFilter(gold)

                    leader = filter.colourFilter[GwentCardColour.LEADER] ?: false
                    view.setLeaderFilter(leader)
                    view.setTypeLeaderFilter(leader)

                    common = filter.rarityFilter[GwentCardRarity.COMMON] ?: false
                    view.setCommonFilter(common)

                    rare = filter.rarityFilter[GwentCardRarity.RARE] ?: false
                    view.setRareFilter(rare)

                    epic = filter.rarityFilter[GwentCardRarity.EPIC] ?: false
                    view.setEpicFilter(epic)

                    legendary = filter.rarityFilter[GwentCardRarity.LEGENDARY] ?: false
                    view.setLegendaryFilter(legendary)

                    minProvisions = filter.minProvisions
                    view.setMinProvisions(minProvisions)

                    maxProvisions = filter.maxProvisions
                    view.setMaxProvisions(filter.maxProvisions)

                    unit = filter.typeFilter[GwentCardType.Unit] ?: false
                    view.setUnitFilter(unit)

                    spell = filter.typeFilter[GwentCardType.Spell] ?: false
                    view.setSpellFilter(spell)

                    artifact = filter.typeFilter[GwentCardType.Artifact] ?: false
                    view.setArtifactFilter(artifact)

                    baseSet = filter.expansionFilter[GwentExpansion.Base] ?: false
                    view.setBaseSetFilter(baseSet)

                    unmillableSet = filter.expansionFilter[GwentExpansion.Unmillable] ?: false
                    view.setUnmillableSetFilter(unmillableSet)

                    tokenSet = filter.expansionFilter[GwentExpansion.Token] ?: false
                    view.setTokenSetFilter(tokenSet)

                    thronebreaker = filter.expansionFilter[GwentExpansion.Thronebreaker] ?: false
                    view.setThronebreakerSetFilter(thronebreaker)

                    crimsonCurse = filter.expansionFilter[GwentExpansion.CrimsonCurse] ?: false
                    view.setCrimsonCurseSetFilter(crimsonCurse)

                    novigrad = filter.expansionFilter[GwentExpansion.Novigrad] ?: false
                    view.setNovigradSetFilter(novigrad)

                    sortedBy = filter.sortedBy
                    view.setSortedBy(sortedBy)
                }
            })
            .addToComposite()

        if (!deckId.isEmpty()) {
            view.hideTokenFilterForDeckBuilder()
            getDeckUseCase.get(deckId)
                .firstOrError()
                .map { it.faction }
                .subscribeWith(object : BaseDisposableSingle<GwentFaction>() {
                    override fun onSuccess(faction: GwentFaction) {
                        view.showFiltersForDeckBuilder(faction)
                    }
                })
                .addToComposite()
        }
    }

    override fun resetFilters() {
        resetFilterUseCase.resetFilter(deckId)
        view.close()
    }

    override fun applyFilters() {
        getFilterUseCase.getFilter(deckId)
            .firstOrError()
            .subscribeWith(object : BaseDisposableSingle<CardFilter>() {
                override fun onSuccess(filter: CardFilter) {
                    val factions = mapOf(
                        GwentFaction.NILFGAARD to nilfgaard,
                        GwentFaction.NORTHERN_REALMS to northernRealms,
                        GwentFaction.MONSTER to monster,
                        GwentFaction.SKELLIGE to skellige,
                        GwentFaction.SCOIATAEL to scoiatael,
                        GwentFaction.NEUTRAL to neutral,
                        GwentFaction.SYNDICATE to syndicate
                    )

                    val colours = mapOf(
                        GwentCardColour.BRONZE to bronze,
                        GwentCardColour.GOLD to gold,
                        GwentCardColour.LEADER to leader
                    )

                    val rarities = mapOf(
                        GwentCardRarity.COMMON to common,
                        GwentCardRarity.RARE to rare,
                        GwentCardRarity.EPIC to epic,
                        GwentCardRarity.LEGENDARY to legendary
                    )

                    val types = mapOf(
                        GwentCardType.Unit to unit,
                        GwentCardType.Spell to spell,
                        GwentCardType.Artifact to artifact,
                        GwentCardType.Leader to leader
                    )

                    val expansions = mapOf(
                        GwentExpansion.Base to baseSet,
                        GwentExpansion.Token to tokenSet,
                        GwentExpansion.Unmillable to unmillableSet,
                        GwentExpansion.Thronebreaker to thronebreaker,
                        GwentExpansion.CrimsonCurse to crimsonCurse,
                        GwentExpansion.Novigrad to novigrad
                    )

                    val newFilter = CardFilter(
                        rarities,
                        colours,
                        factions,
                        types,
                        expansions,
                        minProvisions,
                        maxProvisions,
                        filter.isCollectibleOnly,
                        sortedBy
                    )
                    setFilterUseCase.setFilter(deckId, newFilter)
                    view.close()
                }
            })
            .addToComposite()
    }

    override fun onNilfgaardFilterChanged(checked: Boolean) { nilfgaard = checked }

    override fun onNorthernRealmsFilterChanged(checked: Boolean) { northernRealms = checked }

    override fun onMonsterFilterChanged(checked: Boolean) { monster = checked }

    override fun onSkelligeFilterChanged(checked: Boolean) { skellige = checked }

    override fun onScoiataelFilterChanged(checked: Boolean) { scoiatael = checked }

    override fun onNeutralFilterChanged(checked: Boolean) { neutral = checked }

    override fun onSyndicateFilterChanged(checked: Boolean) { syndicate = checked }

    override fun onBronzeChanged(checked: Boolean) { bronze = checked }

    override fun onGoldChanged(checked: Boolean) { gold = checked }

    override fun onLeaderChanged(checked: Boolean) {
        leader = checked
        view.setTypeLeaderFilter(checked)
    }

    override fun onCommonChanged(checked: Boolean) { common = checked }

    override fun onRareChanged(checked: Boolean) { rare = checked }

    override fun onEpicChanged(checked: Boolean) { epic = checked }

    override fun onLegendaryChanged(checked: Boolean) { legendary = checked }

    override fun onMinProvisionsChanged(min: Int) { minProvisions = min }

    override fun onMaxProvisionsChanged(max: Int) { maxProvisions = max }

    override fun onTypeUnitChanged(checked: Boolean) { unit = checked }

    override fun onTypeArtifactChanged(checked: Boolean) { artifact = checked }

    override fun onTypeSpellChanged(checked: Boolean) { spell = checked }

    override fun onBaseSetChanged(checked: Boolean) { baseSet = checked }

    override fun onTokenSetChanged(checked: Boolean) { tokenSet = checked }

    override fun onUnmillableSetChanged(checked: Boolean) { unmillableSet = checked }

    override fun onThronebreakerSetChanged(checked: Boolean) { thronebreaker = checked }

    override fun onCrimsonCurseSetChanged(checked: Boolean) { crimsonCurse = checked }

    override fun onNovigradSetChanged(checked: Boolean) { novigrad = checked }

    override fun onSortedByChanged(sort: SortedBy) { sortedBy = sort}

    override fun onTypeLeaderChanged(checked: Boolean) {
        leader = checked
        view.setLeaderFilter(checked)
    }

    override fun onRefresh() {
        // Do nothing
    }

    override fun setDeck(deckId: String) {
        this.deckId = deckId
    }
}
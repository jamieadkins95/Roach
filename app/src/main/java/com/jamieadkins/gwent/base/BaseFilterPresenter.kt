package com.jamieadkins.gwent.base

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.card.CardFilterListener
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.Loyalty
import com.jamieadkins.gwent.model.Rarity

abstract class BaseFilterPresenter<V> : BasePresenter<V>(), CardFilterListener {
    lateinit var cardFilter: CardFilter
    var searchQuery: String? = null

    fun clearFilters() {
        cardFilter.clearFilters()
        onCardFilterUpdated()
    }

    fun onRarityFilterChanged(filter: Rarity, checked: Boolean){
        cardFilter.rarityFilter[filter] = checked
    }

    fun onFactionFilterChanged(filter: GwentFaction, checked: Boolean){
        cardFilter.factionFilter[filter] = checked
    }

    fun onColourFilterChanged(filter: CardColour, checked: Boolean){
        cardFilter.colourFilter[filter] = checked
    }

    fun onLoyaltyFilterChanged(filter: Loyalty, checked: Boolean){
        cardFilter.loyaltyFilter[filter] = checked
    }

    fun updateSearchQuery(query: String?) {
        searchQuery = if (query == "") null else query
        searchQuery?.let {
            searchQuery = it.substring(0, 1).toUpperCase() + it.substring(1)
        }
        onCardFilterUpdated()
    }
}
package com.jamieadkins.gwent.base

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.card.CardFilterListener

abstract class BaseFilterPresenter<V> : BasePresenter<V>(), CardFilterListener {
    lateinit var cardFilter: CardFilter
    var searchQuery: String? = null

    fun clearFilters() {
        cardFilter.clearFilters()
        onCardFilterUpdated()
    }

    fun updateFilter(key: String, state: Boolean) {
        cardFilter.put(key, state)
    }

    fun updateSearchQuery(query: String?) {
        searchQuery = if (query == "") null else query
        searchQuery?.let {
            searchQuery = it.substring(0, 1).toUpperCase() + it.substring(1)
        }
        onCardFilterUpdated()
    }
}
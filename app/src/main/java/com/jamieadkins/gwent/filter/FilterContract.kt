package com.jamieadkins.gwent.filter

interface FilterContract {
    interface View {
        fun showFilters(filterableItems: List<FilterableItem>)
    }

    interface Presenter
}

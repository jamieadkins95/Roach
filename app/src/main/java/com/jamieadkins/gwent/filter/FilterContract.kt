package com.jamieadkins.gwent.filter

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
    }

    interface Presenter : MvpPresenter {

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
    }
}

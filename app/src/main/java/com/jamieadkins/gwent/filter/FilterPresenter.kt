package com.jamieadkins.gwent.filter

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.FilterChangeEvent
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.repository.filter.FilterRepository

class FilterPresenter(val filterType: FilterType,
                      schedulerProvider: BaseSchedulerProvider,
                      val filterRepository: FilterRepository) :
        BasePresenter<FilterContract.View>(schedulerProvider), FilterContract.Presenter {

    override fun onAttach(newView: FilterContract.View) {
        super.onAttach(newView)
        RxBus.register(FilterChangeEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<FilterChangeEvent>() {
                    override fun onNext(event: FilterChangeEvent) {
                        val filter = event.data
                        when (filter) {
                            is FactionFilter -> filterRepository.updateFactionFilter(filter.faction, filter.isChecked)
                            is ColourFilter -> filterRepository.updateColourFilter(filter.colour, filter.isChecked)
                            is RarityFilter -> filterRepository.updateRarityFilter(filter.rarity, filter.isChecked)
                        }
                    }
                })
                .addToComposite(disposable)

        filterRepository.getFilter()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableObserver<CardFilter>() {
                    override fun onNext(cardFilter: CardFilter) {
                        val filters = mutableListOf<FilterableItem>()
                        when (filterType) {
                            FilterType.FACTION -> {
                                cardFilter.factionFilter.forEach {
                                    filters.add(FactionFilter(it.key, it.value))
                                }
                            }
                            FilterType.COLOUR -> {
                                cardFilter.colourFilter.forEach {
                                    filters.add(ColourFilter(it.key, it.value))
                                }
                            }
                            FilterType.RARITY -> {
                                cardFilter.rarityFilter.forEach {
                                    filters.add(RarityFilter(it.key, it.value))
                                }
                            }
                        }
                        view?.showFilters(filters)
                    }
                })
    }

    override fun onRefresh() {
        // Do nothing
    }
}
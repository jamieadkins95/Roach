package com.jamieadkins.gwent.filter

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.FilterChangeEvent
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository

class FilterPresenter(val filterType: FilterType,
                      schedulerProvider: BaseSchedulerProvider,
                      val filterRepository: FilterRepository) :
        BasePresenter<FilterContract.View>(schedulerProvider), FilterContract.Presenter {

    override fun onAttach(newView: FilterContract.View) {
        super.onAttach(newView)

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
                    }
                })
    }

    override fun onRefresh() {
        // Do nothing
    }
}
package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.filter.FilterRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.filter.*
import io.reactivex.functions.BiFunction

class CardsPresenter(schedulerProvider: BaseSchedulerProvider,
                     val cardRepository: CardRepository,
                     val updateRepository: UpdateRepository,
                     val filterRepository: FilterRepository) :
        BasePresenter<CardDatabaseContract.View>(schedulerProvider), CardDatabaseContract.Presenter {

    override fun onAttach(newView: CardDatabaseContract.View) {
        super.onAttach(newView)

        RxBus.register(RefreshEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<RefreshEvent>() {
                    override fun onNext(t: RefreshEvent) {
                        onRefresh()
                    }
                })
                .addToComposite(disposable)

        RxBus.register(OpenFilterMenuEvent::class.java)
                .withLatestFrom(filterRepository.getFilter(),
                        BiFunction { event: OpenFilterMenuEvent, filter: CardFilter ->
                            Pair(event.data, filter)
                        })
                .subscribeWith(object : BaseDisposableObserver<Pair<FilterType, CardFilter>>() {
                    override fun onNext(pair: Pair<FilterType, CardFilter>) {
                        val filters = mutableListOf<FilterableItem>()
                        when (pair.first) {
                            FilterType.FACTION -> {
                                pair.second.factionFilter.forEach {
                                    filters.add(FactionFilter(it.key, it.value))
                                }
                            }
                            FilterType.COLOUR -> {
                                pair.second.colourFilter.forEach {
                                    filters.add(ColourFilter(it.key, it.value))
                                }
                            }
                            FilterType.RARITY -> {
                                pair.second.rarityFilter.forEach {
                                    filters.add(RarityFilter(it.key, it.value))
                                }
                            }
                        }
                        view?.showFilterMenu(filters)
                    }
                })
                .addToComposite(disposable)

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
                .observeOn(schedulerProvider.ui())
                .doOnNext { view?.setLoadingIndicator(true) }
                .observeOn(schedulerProvider.io())
                .switchMapSingle { cardRepository.getCards(it) }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableObserver<Collection<GwentCard>>() {
                    override fun onNext(list: Collection<GwentCard>) {
                        view?.showCards(list.toMutableList())
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        updateRepository.isUpdateAvailable()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Boolean>() {
                    override fun onSuccess(update: Boolean) {
                        if (update) {
                            view?.showUpdateAvailable()
                            view?.setLoadingIndicator(false)
                        }
                    }
                })
                .addToComposite(disposable)
    }

    override fun search(query: String?) {

    }

    override fun clearSearch() {

    }
}

package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.core.GwentStringHelper
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.filter.*

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(schedulerProvider: BaseSchedulerProvider,
                     val cardRepository: CardRepository,
                     val updateRepository: UpdateRepository) :
        BasePresenter<CardDatabaseContract.View>(schedulerProvider), CardDatabaseContract.Presenter {

    private val cardFilter = CardFilter()

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
                .subscribeWith(object : BaseDisposableObserver<OpenFilterMenuEvent>() {
                    override fun onNext(event: OpenFilterMenuEvent) {
                        val filters = mutableListOf<FilterableItem>()
                        when (event.data) {
                            FilterType.FACTION -> {
                                cardFilter.factionFilter.entries.forEach {
                                    filters.add(FactionFilter(it.key, it.value))
                                }
                            }
                            FilterType.COLOUR -> {
                                cardFilter.colourFilter.entries.forEach {
                                    filters.add(ColourFilter(it.key, it.value))
                                }
                            }
                            FilterType.RARITY -> {
                                cardFilter.rarityFilter.entries.forEach {
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
                            is FactionFilter -> cardFilter.factionFilter[filter.faction] = filter.isChecked
                            is ColourFilter -> cardFilter.colourFilter[filter.colour] = filter.isChecked
                            is RarityFilter -> cardFilter.rarityFilter[filter.rarity] = filter.isChecked
                        }
                    }
                })
                .addToComposite(disposable)

        RxBus.register(CloseFilterMenuEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<CloseFilterMenuEvent>() {
                    override fun onNext(t: CloseFilterMenuEvent) {
                        onRefresh()
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
                        }
                    }
                })
                .addToComposite(disposable)

        loadCardData()
    }

    private fun loadCardData() {
        view?.setLoadingIndicator(true)
        val source = if (cardFilter.searchQuery != null) {
            cardRepository.searchCards(cardFilter.searchQuery ?: "")
        } else {
            cardRepository.getCards(cardFilter)
        }
        source
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(list: Collection<GwentCard>) {
                        view?.showCards(list.toMutableList())
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }

    override fun search(query: String?) {
        cardFilter.searchQuery = query
        loadCardData()
    }

    override fun clearSearch() {
        cardFilter.searchQuery = null
        loadCardData()
    }
}

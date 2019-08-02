package com.jamieadkins.gwent.domain.filter

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import io.reactivex.Single
import javax.inject.Inject

class FilterCardsUseCase @Inject constructor(
    private val schedulerProvider: SchedulerProvider
) {

    fun filter(cards: List<GwentCard>, filter: CardFilter): Single<List<GwentCard>> {
        return Single.fromCallable { cards.filter { doesCardMeetFilter(it, filter) } }
            .map { filtered ->
                when (filter.sortedBy) {
                    SortedBy.SEARCH_RELEVANCE -> filtered
                    SortedBy.ALPHABETICALLY_ASC -> filtered.sortedBy { it.name }
                    SortedBy.ALPHABETICALLY_DESC -> filtered.sortedByDescending { it.name }
                    SortedBy.STRENGTH_ASC -> filtered.sortedBy { it.strength }
                    SortedBy.STRENGTH_DESC -> filtered.sortedByDescending { it.strength }
                }
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    private fun doesCardMeetFilter(card: GwentCard, filter: CardFilter): Boolean {
        val include = !filter.isCollectibleOnly || card.collectible
        val faction = filter.factionFilter[card.faction] ?: false || filter.factionFilter[card.secondaryFaction] ?: false
        val rarity = filter.rarityFilter[card.rarity] ?: false
        val colour = filter.colourFilter[card.colour] ?: false
        val type = filter.typeFilter[card.type] ?: false
        val provisions = card.provisions >= filter.minProvisions && card.provisions <= filter.maxProvisions
        return (faction && rarity && colour && include && provisions && type)
    }

}
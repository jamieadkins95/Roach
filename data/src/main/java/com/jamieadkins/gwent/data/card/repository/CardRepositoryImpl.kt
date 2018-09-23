package com.jamieadkins.gwent.data.card.repository

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.CardDatabaseResult
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.data.CardSearch
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.data.CardSearchData
import com.jamieadkins.gwent.data.card.mapper.GwentCardMapper
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import io.reactivex.Single
import io.reactivex.functions.Function3

class CardRepositoryImpl(private val database: GwentDatabase,
                         private val cardMapper: GwentCardMapper) : CardRepository {

    private fun getAllCards(): Single<Collection<GwentCard>> {
        return getCardEntities()
                .map { cardMapper.mapList(it, "en-US") }
    }

    private fun getCardEntities(): Single<List<CardWithArtEntity>> {
        return database.cardDao().getCards()
    }

    override fun getCards(cardFilter: CardFilter): Single<CardDatabaseResult> {
        val source = if (cardFilter.searchQuery.isNotEmpty()) {
            Single.zip(
                    getCardEntities(),
                    database.keywordDao().getAllKeywords(),
                    database.categoryDao().getAllCategories(),
                    Function3<List<CardWithArtEntity>, List<KeywordEntity>, List<CategoryEntity>, CardSearchData>
                    { cards, keywords, categories -> CardSearchData(cards, keywords, categories) })
                    .flatMap { CardSearch.searchCards(cardFilter.searchQuery, it) }
                    .flatMap { getCards(it) }
        } else {
            getAllCards()
        }
        return source
                .map { it.filter { card -> cardFilter.let { doesCardMeetFilter(cardFilter, card) } } }
                .map {
                    when (cardFilter.sortedBy) {
                        SortedBy.SEARCH_RELEVANCE -> it
                        SortedBy.ALPHABETICALLY_ASC -> it.sortedBy { it.name }
                        SortedBy.ALPHABETICALLY_DESC -> it.sortedByDescending { it.name }
                        SortedBy.STRENGTH_ASC -> it.sortedBy { it.strength ?: 0 }
                        SortedBy.STRENGTH_DESC -> it.sortedByDescending { it.strength ?: 0 }
                    }
                }
                .map {
                    val factions = cardFilter.factionFilter.entries
                            .filter { it.value }
                            .map { it.key }
                    val colours = cardFilter.colourFilter.entries
                            .filter { it.value }
                            .map { it.key }
                    val rarities = cardFilter.rarityFilter.entries
                            .filter { it.value }
                            .map { it.key }
                    CardDatabaseResult(it, cardFilter.searchQuery, factions, colours,
                            rarities, cardFilter.isCollectibleOnly, cardFilter.sortedBy)
                }
    }

    override fun getCards(cardIds: List<String>): Single<Collection<GwentCard>> {
        return database.cardDao().getCards(cardIds)
                .map { cardMapper.mapList(it, "en-US") }
    }

    override fun getCard(id: String): Single<GwentCard> {
        return database.cardDao().getCard(id)
                .map { cardMapper.map(it, "en-US") }
    }

    fun doesCardMeetFilter(filter: CardFilter, card: GwentCard): Boolean {
        val include = !filter.isCollectibleOnly || card.collectible
        val faction = filter.factionFilter[card.faction] ?: false
        val rarity = filter.rarityFilter[card.rarity] ?: false
        val colour = filter.colourFilter[card.colour] ?: false
        return (faction && rarity && colour && include)
    }
}
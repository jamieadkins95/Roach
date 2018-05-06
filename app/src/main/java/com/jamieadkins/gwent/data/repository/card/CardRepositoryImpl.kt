package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.core.CardDatabaseResult
import com.jamieadkins.gwent.core.SortedBy
import com.jamieadkins.gwent.data.CardSearch
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.data.CardSearchData
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import io.reactivex.Single
import io.reactivex.functions.Function3

class CardRepositoryImpl(private val database: GwentDatabase) : CardRepository {

    private fun getAllCards(): Single<Collection<GwentCard>> {
        return getCardEntities()
                .map { GwentCardMapper.gwentCardListFromCardEntityList(it) }
    }

    private fun getCardEntities(): Single<List<CardEntity>> {
        return database.cardDao().getCards()
                .flatMap { cards -> mergeCardEntitiesWithCardArt(cards) }
    }

    private fun mergeCardEntitiesWithCardArt(cards: List<CardEntity>): Single<List<CardEntity>> {
        val cardIds = cards.map { it.id }
        return database.cardDao().getCardArt(cardIds).map { artList ->
            val artMap = mutableMapOf<String, MutableList<ArtEntity>>()
            artList.forEach {
                if (artMap[it.cardId] == null) {
                    artMap[it.cardId] = mutableListOf()
                }
                artMap[it.cardId]?.add(it)
            }
            cards.forEach {
                it.art = artMap[it.id]
            }
            cards
        }
    }

    override fun getCards(cardFilter: CardFilter): Single<CardDatabaseResult> {
        val source = if (cardFilter.searchQuery.isNotEmpty()) {
            Single.zip(
                    getCardEntities(),
                    database.keywordDao().getAllKeywords(),
                    database.categoryDao().getAllCategories(),
                    Function3<List<CardEntity>, List<KeywordEntity>, List<CategoryEntity>, CardSearchData>
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
                .flatMap { cards -> mergeCardEntitiesWithCardArt(cards) }
                .map { GwentCardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun getCard(id: String): Single<GwentCard> {
        return database.cardDao().getCard(id)
                .flatMap { mergeCardEntitiesWithCardArt(listOf(it)) }
                .map { it.first() }
                .map { GwentCardMapper.cardEntityToGwentCard(it) }
    }

    fun doesCardMeetFilter(filter: CardFilter, card: GwentCard): Boolean {
        val include = !filter.isCollectibleOnly || card.collectible
        val faction = filter.factionFilter[card.faction] ?: false
        val rarity = filter.rarityFilter[card.rarity] ?: false
        val colour = filter.colourFilter[card.colour] ?: false
        return (faction && rarity && colour && include)
    }
}
package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardSearch
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.core.GwentCard
import io.reactivex.Single

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

    override fun getCards(cardFilter: CardFilter?): Single<Collection<GwentCard>> {
        return getAllCards()
                .map { it.filter { cardFilter?.doesCardMeetFilter(it) ?: true } }
    }

    override fun getCards(cardIds: List<String>): Single<Collection<GwentCard>> {
        return database.cardDao().getCards(cardIds)
                .flatMap { cards -> mergeCardEntitiesWithCardArt(cards) }
                .map { GwentCardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun searchCards(query: String): Single<Collection<GwentCard>> {
        return getAllCards()
                /*.flatMap { cardList ->
                    val searchResults = CardSearch.searchCards(query, cardList.toList())
                    getCards(searchResults)
                }*/
    }

    override fun getCard(id: String): Single<GwentCard> {
        return database.cardDao().getCard(id)
                .flatMap { mergeCardEntitiesWithCardArt(listOf(it)) }
                .map { it.first() }
                .map { GwentCardMapper.cardEntityToGwentCard(it) }
    }
}
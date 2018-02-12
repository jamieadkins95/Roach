package com.jamieadkins.gwent.data.repository.card

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardSearch
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.model.GwentCard
import io.reactivex.Single

class CardRepositoryImpl : CardRepository {

    private val database = GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext)

    private fun getAllCards(): Single<Collection<GwentCard>> {
        return database.cardDao().getCards()
                .map { CardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun getCards(cardFilter: CardFilter?): Single<Collection<GwentCard>> {
        return getAllCards()
                .map {
                    it.filter { cardFilter?.doesCardMeetFilter(it) ?: true }
                }
    }

    override fun getCards(cardIds: List<String>): Single<Collection<GwentCard>> {
        return database.cardDao().getCards(cardIds).map { CardMapper.gwentCardListFromCardEntityList(it) }
    }

    override fun searchCards(query: String): Single<Collection<GwentCard>> {
        return getAllCards().flatMap { cardList ->
            val searchResults = CardSearch.searchCards(query, cardList.toList())
            getCards(searchResults)
        }
    }

    override fun getCard(id: String): Single<GwentCard> {
        return database.cardDao().getCard(id)
                .map { CardMapper.cardEntityToGwentCard(it) }
    }
}
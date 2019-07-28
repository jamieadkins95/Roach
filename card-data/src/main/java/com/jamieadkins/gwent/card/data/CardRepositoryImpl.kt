package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.functions.Function5
import timber.log.Timber
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val database: GwentDatabase,
    private val cardMapper: GwentCardMapper,
    private val fromFactionMapper: FromFactionMapper,
    private val localeRepository: LocaleRepository
) : CardRepository {

    private val cardMemoryCache = mutableMapOf<String, GwentCard>()

    private fun getCardEntities(): Observable<List<CardWithArtEntity>> {
        return database.cardDao().getCards().toObservable()
    }

    override fun searchCards(searchQuery: String, quickSearch: Boolean): Observable<List<GwentCard>> {
        return searchCardsInFactions(searchQuery, GwentFaction.values().toList(), quickSearch)
    }

    override fun searchCardsInFactions(searchQuery: String, factions: List<GwentFaction>, quickSearch: Boolean): Observable<List<GwentCard>> {
        val factions = factions.map { fromFactionMapper.map(it) }
        return Observable.combineLatest(
            database.cardDao().getCardsInFactions(factions).toObservable(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            localeRepository.getDefaultLocale(),
            Function5 { cards: List<CardWithArtEntity>,
                        keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>,
                        userLocale: String,
                        defaultLocale: String ->
                val cardSearchData = CardSearchData(cards, keywords, categories)
                if (quickSearch) {
                    CardSearch.quickSearch(searchQuery, cardSearchData, userLocale, defaultLocale)
                } else {
                    CardSearch.searchCards(searchQuery, cardSearchData, userLocale, defaultLocale)
                }
            })
            .switchMap { getCards(it) }
    }

    override fun getCards(): Observable<List<GwentCard>> {
        return Observable.combineLatest(
            getCardEntities(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            Function4 { cards: List<CardWithArtEntity>, keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>, locale: String ->
                cardMapper.mapList(cards, locale, keywords, categories)
            })
            .doOnNext {
                it.forEach { card -> cardMemoryCache[card.id] = card }
            }
    }

    override fun getCards(cardIds: List<String>): Observable<List<GwentCard>> {
        val fromDb = Observable.combineLatest(
            database.cardDao().getCards(cardIds).toObservable(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            Function4 { cards: List<CardWithArtEntity>, keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>, locale: String ->
                cardMapper.mapList(cards, locale, keywords, categories)
            })
            .doOnNext {
                it.forEach { card -> cardMemoryCache[card.id] = card }
            }

        // Can use the cache if it contains all the cards.
        val canUseCache = cardIds.all { cardMemoryCache.contains(it) }
        if (canUseCache) {
            Timber.i("All cards requested are in the cache.")
            val fromCache = cardIds.mapNotNull { cardMemoryCache[it] }
            return fromDb.startWith(fromCache)
        } else {
            Timber.i("Can't use the card cache as there are cards missing.")
        }

        return fromDb
    }

    override fun getCard(id: String): Observable<GwentCard> {
        val fromDb = Observable.combineLatest(
            database.cardDao().getCard(id).toObservable(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            Function4 { card: CardWithArtEntity, keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>, locale: String ->
                cardMapper.map(card, locale, keywords, categories)
            })

        val fromCache = cardMemoryCache[id]
        if (fromCache != null) {
            Timber.i("Card ${fromCache.name} is in the cache.")
            return fromDb.startWith(fromCache)
        } else {
            Timber.i("Card $id is not in the cache.")
        }

        return fromDb
    }

    override fun getLeaders(faction: GwentFaction): Observable<List<GwentCard>> {
        return Observable.combineLatest(
            database.cardDao().getLeaders(fromFactionMapper.map(faction)).toObservable(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            Function4 { cards: List<CardWithArtEntity>, keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>, locale: String ->
                cardMapper.mapList(cards, locale, keywords, categories)
            })
    }

    override fun getCardsInFactions(factions: List<GwentFaction>): Observable<List<GwentCard>> {
        val factions = factions.map { fromFactionMapper.map(it) }
        return Observable.combineLatest(
            database.cardDao().getCardsInFactions(factions).toObservable(),
            database.keywordDao().getAllKeywords().toObservable(),
            database.categoryDao().getAllCategories().toObservable(),
            localeRepository.getLocale(),
            Function4 { cards: List<CardWithArtEntity>, keywords: List<KeywordEntity>,
                        categories: List<CategoryEntity>, locale: String ->
                cardMapper.mapList(cards, locale, keywords, categories)
            })
    }

    override fun invalidateMemoryCache() {
        cardMemoryCache.clear()
        Timber.i("Card cache invalidated.")
    }

    private fun getLocalisedKeywords(): Observable<List<KeywordEntity>> {
        return localeRepository.getLocale()
            .switchMap {
                database.keywordDao().getKeywordsForLocale(it).toObservable()
            }
    }

    private fun getLocalisedCategories(): Observable<List<CategoryEntity>> {
        return localeRepository.getLocale()
            .switchMap {
                database.categoryDao().getCategoriesForLocale(it).toObservable()
            }
    }
}
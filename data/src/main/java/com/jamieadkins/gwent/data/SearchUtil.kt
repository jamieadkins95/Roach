package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import io.reactivex.Single
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.util.*

object CardSearch {

    private const val MIN_SCORE = 50

    fun searchCards(query: String, cardSearchData: CardSearchData): Single<List<String>> {
        return Single.fromCallable {
            val searchResults = mutableListOf<CardSearchResult>()
            val cardIds = mutableListOf<String>()
            var maxScore = 0
            val start = System.currentTimeMillis()
            cardSearchData.cards.forEach { card ->
                val scores = ArrayList<Int>()
                scores.add(FuzzySearch.partialRatio(query, card.card.name["en-US"]))
                scores.add(FuzzySearch.partialRatio(query, card.card.tooltip["en-US"]))
                card.card.categoryIds.forEach { categoryId ->
                    cardSearchData.categories.filter { categoryId == it.categoryId }.forEach {
                        scores.add(FuzzySearch.partialRatio(query, it.name))
                    }
                }
                card.card.keywordIds.forEach { keywordId ->
                    cardSearchData.keywords.filter { keywordId == it.keywordId }.forEach {
                        scores.add(FuzzySearch.partialRatio(query, it.name))
                    }
                }
                val score = Collections.max(scores)
                if (score > maxScore) {
                    maxScore = score
                }
                card.card.id.let {
                    val result = CardSearchResult(it, score)
                    searchResults.add(result)
                }
            }

            searchResults.sortByDescending { result -> result.score }
            val cutOff = maxScore - maxScore / 10
            searchResults.forEach {
                // Catch anything within 20%
                if (it.score >= cutOff && it.score >= MIN_SCORE) {
                    cardIds.add(it.cardId)
                } else {
                    // The list is sorted, so by the time we drop below the cutoff, there is no point
                    // looking at other cards.
                    return@fromCallable cardIds
                }
            }
            return@fromCallable cardIds
        }
    }
}

private class CardSearchResult(val cardId: String, val score: Int)

class CardSearchData(val cards: List<CardWithArtEntity>, val keywords: List<KeywordEntity>, val categories: List<CategoryEntity>)
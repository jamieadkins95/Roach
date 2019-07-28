package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import me.xdrop.fuzzywuzzy.FuzzySearch
import timber.log.Timber
import java.util.ArrayList
import java.util.Collections

object CardSearch {

    private const val MIN_SCORE = 50

    fun searchCards(query: String, cardSearchData: CardSearchData, userLocale: String, defaultLocale: String): List<String> {
        val searchResults = mutableListOf<CardSearchResult>()
        var maxScore = 0
        val start = System.currentTimeMillis()
        cardSearchData.cards.forEach { card ->
            val scores = ArrayList<Int>()
            val lowerCaseQuery = query.toLowerCase()

            // Search in user's language
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.name[userLocale]?.toLowerCase()))
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.tooltip[userLocale]?.toLowerCase()))

            // Search in the default language
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.name[defaultLocale]?.toLowerCase()))
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.tooltip[defaultLocale]?.toLowerCase()))

            card.card.categoryIds.forEach { categoryId ->
                cardSearchData.categories.filter { categoryId == it.categoryId }.forEach {
                    scores.add(FuzzySearch.partialRatio(lowerCaseQuery, it.name.toLowerCase()))
                }
            }
            card.card.keywordIds.forEach { keywordId ->
                cardSearchData.keywords.filter { keywordId == it.keywordId }.forEach {
                    scores.add(FuzzySearch.partialRatio(lowerCaseQuery, it.name.toLowerCase()))
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

        val cards = sortResults(searchResults, maxScore)
        val end = System.currentTimeMillis()
        val timeTaken = end - start
        Timber.d("Search time = $timeTaken ms")
        return cards
    }

    /**
     * Only searches by name.
     */
    fun quickSearch(query: String, cardSearchData: CardSearchData, userLocale: String, defaultLocale: String): List<String> {
        val searchResults = mutableListOf<CardSearchResult>()
        var maxScore = 0
        val start = System.currentTimeMillis()
        cardSearchData.cards.forEach { card ->
            val scores = ArrayList<Int>()
            val lowerCaseQuery = query.toLowerCase()

            // Search in user's language
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.name[userLocale]?.toLowerCase()))
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.tooltip[userLocale]?.toLowerCase()))

            // Search in the default language
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.name[defaultLocale]?.toLowerCase()))
            scores.add(FuzzySearch.partialRatio(lowerCaseQuery, card.card.tooltip[defaultLocale]?.toLowerCase()))
            val score = Collections.max(scores)
            if (score > maxScore) {
                maxScore = score
            }
            card.card.id.let {
                val result = CardSearchResult(it, score)
                searchResults.add(result)
            }
        }

        val cards = sortResults(searchResults, maxScore)
        val end = System.currentTimeMillis()
        val timeTaken = end - start
        Timber.d("Quick Search time = $timeTaken ms")
        return cards
    }

    private fun sortResults(searchResults: List<CardSearchResult>, maxScore: Int): List<String> {
        val cardIds = mutableListOf<String>()
        val sorted = searchResults.sortedByDescending { result -> result.score }
        val cutOff = maxScore - maxScore / 10
        sorted.forEach {
            // Catch anything within 20%
            if (it.score >= cutOff && it.score >= MIN_SCORE) {
                cardIds.add(it.cardId)
            } else {
                // The list is sorted, so by the time we drop below the cutoff, there is no point
                // looking at other cards.
                return cardIds
            }
        }
        return cardIds
    }
}

private class CardSearchResult(val cardId: String, val score: Int)

class CardSearchData(val cards: List<CardWithArtEntity>, val keywords: List<KeywordEntity>, val categories: List<CategoryEntity>)
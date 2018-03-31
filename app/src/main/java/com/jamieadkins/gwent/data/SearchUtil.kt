package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import io.reactivex.Single
import me.xdrop.fuzzywuzzy.FuzzySearch
import timber.log.Timber
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
                GwentApplication.INSTANCE.resources.getStringArray(R.array.locales).forEach {
                    locale ->
                    scores.add(FuzzySearch.partialRatio(query, card.name[locale]))
                    scores.add(FuzzySearch.partialRatio(query, card.tooltip[locale]))
                }
                card.categoryIds.forEach { categoryId ->
                    cardSearchData.categories.filter { categoryId == it.categoryId }.forEach {
                        scores.add(FuzzySearch.partialRatio(query, it.name))
                    }
                }
                card.keywordIds.forEach { keywordId ->
                    cardSearchData.keywords.filter { keywordId == it.keywordId }.forEach {
                        scores.add(FuzzySearch.partialRatio(query, it.name))
                    }
                }
                val score = Collections.max(scores)
                if (score > maxScore) {
                    maxScore = score
                }
                card.id.let {
                    val result = CardSearchResult(it, score)
                    searchResults.add(result)
                }
            }

            val timeTaken = System.currentTimeMillis() - start
            Timber.d("Search took $timeTaken ms")

            searchResults.sortByDescending { result -> result.score }
            val cutOff = maxScore - maxScore / 20
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

class CardSearchData(val cards: List<CardEntity>, val keywords: List<KeywordEntity>, val categories: List<CategoryEntity>)
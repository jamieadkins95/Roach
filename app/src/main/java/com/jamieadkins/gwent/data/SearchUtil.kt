package com.jamieadkins.gwent.data

import me.xdrop.fuzzywuzzy.FuzzySearch
import java.util.*

fun searchCards(query: String, cardList: List<CardDetails>, locale: String): List<String> {
    val searchResults = ArrayList<CardSearchResult>()
    val cardIds = ArrayList<String>()
    var maxScore = 0
    cardList.forEach { card ->
        if (card.isReleased) {
            val scores = ArrayList<Int>()
            scores.add(FuzzySearch.partialRatio(query, card.getName(locale)))
            scores.add(FuzzySearch.partialRatio(query, card.getInfo(locale)))
            card.categories?.forEach {
                scores.add(FuzzySearch.partialRatio(query, it))
            }
            val score = Collections.max(scores)
            if (score > maxScore) {
                maxScore = score
            }
            val result = CardSearchResult(card.ingameId, score)
            searchResults.add(result)
        }
    }

    searchResults.sortByDescending { result -> result.score }
    val cutOff = maxScore - maxScore / 20
    searchResults.forEach {
        // Catch anything within 20%
        if (it.score >= cutOff) {
            cardIds.add(it.cardId)
        } else {
            // The list is sorted, so by the time we drop below the cutoff, there is no point
            // looking at other cards.
            return cardIds
        }
    }
    return cardIds
}

private class CardSearchResult(val cardId: String, val score: Int)
package com.jamieadkins.gwent.data

import android.util.Log
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.util.*

fun searchCards (query: String, cardList: List<CardDetails>, locale: String): List<String> {
    val searchResults = ArrayList<CardSearchResult>()
    val cardIds = ArrayList<String>()
    var maxScore = 0
    cardList.forEach { card ->
        if (card.isReleased) {
            val scores = ArrayList<Int>()
            scores.add(FuzzySearch.partialRatio(query, card.getName(locale)))
            scores.add(FuzzySearch.partialRatio(query, card.getInfo(locale)))
            val score = Collections.max(scores)
            if (score > maxScore) {
                maxScore = score
            }
            val result = CardSearchResult(card.ingameId, score)
            searchResults.add(result)
        }
    }

    searchResults.forEach {
        // Catch anything within 20%
        if (it.score >= maxScore - maxScore / 20) {
            cardIds.add(it.cardId)
        }
    }
    return cardIds
}

class CardSearchResult(val cardId: String, val score: Int)
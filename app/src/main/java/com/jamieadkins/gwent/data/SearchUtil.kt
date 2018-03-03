package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.model.GwentCard
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.util.*

object CardSearch {

    private const val MIN_SCORE = 50

    fun searchCards(query: String, cardList: List<GwentCard>): List<String> {
        val searchResults = ArrayList<CardSearchResult>()
        val cardIds = ArrayList<String>()
        var maxScore = 0
        cardList.forEach { card ->
            val scores = ArrayList<Int>()
            GwentApplication.INSTANCE.resources.getStringArray(R.array.locales).forEach {
                locale ->
                scores.add(FuzzySearch.partialRatio(query, card.name[locale]))
                scores.add(FuzzySearch.partialRatio(query, card.info[locale]))
            }
            card.categories?.forEach {
                scores.add(FuzzySearch.partialRatio(query, it))
            }
            val score = Collections.max(scores)
            if (score > maxScore) {
                maxScore = score
            }
            card.id?.let {
                val result = CardSearchResult(it, score)
                searchResults.add(result)
            }
        }

        searchResults.sortByDescending { result -> result.score }
        val cutOff = maxScore - maxScore / 20
        searchResults.forEach {
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
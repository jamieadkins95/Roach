package com.jamieadkins.gwent.model

data class GwentDeckCardCounts(
        val bronzeCardCount: Int = 0,
        val silverCardCount: Int = 0,
        val goldCardCount: Int = 0) {

    val totalCardCount = bronzeCardCount + silverCardCount + goldCardCount
}
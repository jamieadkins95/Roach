package com.jamieadkins.gwent.core

data class CardDatabaseResult(val cards: List<GwentCard> = emptyList(),
                              val searchQuery: String = "",
                              val factions: List<GwentFaction> = emptyList(),
                              val colours: List<GwentCardColour> = emptyList(),
                              val rarities: List<GwentCardRarity> = emptyList(),
                              val collectibleCardsOnly: Boolean = false,
                              val sortedBy: SortedBy = SortedBy.ALPHABETICALLY_ASC)
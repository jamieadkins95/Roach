package com.jamieadkins.gwent.deck


sealed class DeckBuilderEvent(val cardId: String) {

    class CardDatabaseClick(cardId: String): DeckBuilderEvent(cardId)

    class CardDatabaseLongClick(cardId: String): DeckBuilderEvent(cardId)

    class DeckClick(cardId: String): DeckBuilderEvent(cardId)

    class DeckLongClick(cardId: String): DeckBuilderEvent(cardId)

    class LeaderClick(cardId: String): DeckBuilderEvent(cardId)

    class LeaderLongClick(cardId: String): DeckBuilderEvent(cardId)
}
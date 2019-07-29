package com.jamieadkins.gwent.decktracker.bus

sealed class DeckTrackerEvent {

    data class CardPlayed(val cardId: String) : DeckTrackerEvent()
}
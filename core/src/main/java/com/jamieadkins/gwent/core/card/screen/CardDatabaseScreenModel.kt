package com.jamieadkins.gwent.core.card.screen

import com.jamieadkins.gwent.core.CardDatabaseResult

data class CardDatabaseScreenModel(val cards: CardDatabaseResult, val updateAvailable: Boolean)